/**
 * @author: Daniele Grignani
 * @date: Nov 1, 2015
 */

package com.rat.init;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;

import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.main.SystemInitializerHelpers;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.AppProperties;
import com.dgr.utils.Utils;


public class InitDB {
	public enum Command{
		AddUser("addUser"),
		AddDomain("addDomain"),
		BindUserToDomain("bind");
		
		private final String _stringValue;
		
		private Command(final String stringValue) { 
			_stringValue = stringValue; 
		}
		
		public String toString() { 
			return _stringValue; 
		}
		
	    public static JSONType fromString(String string) {
	        if (string != null){
	            for (JSONType type : JSONType.values()){
	                if (string.equalsIgnoreCase(type.toString())){
	                    return type;
	                }
	            }
	        }
	        
	        throw new IllegalArgumentException(String.format("\"%s\" is not a valid Status.", string));
	    }
	}
	
	private Connection _connect = null;
	private String _rootDomainUUID = null;
	private Command _status = Command.AddUser;
	private String _userUUID = null;
	private String _domainUUID = null;
	private String _domain = null;
	private String _userName = null;
	private FileSystemXmlApplicationContext _context = null;
	public InitDB() {
		// TODO Auto-generated constructor stub
	}
	
	public void closeDB() throws Exception{
		if(_connect != null){
			_connect.close();
		}
		
		_context.close();
		RATSessionManager.getInstance().shutdown();
	}
	
	public void openDB() throws Exception{
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			
			_context = new FileSystemXmlApplicationContext(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-producer-unitTest.xml");
			RATSessionManager.init();
			
			Class.forName("com.mysql.jdbc.Driver");
			_connect = DriverManager.getConnection("jdbc:mysql://localhost/ratwsserver?" + "user=admin&password=admin");
			
			this.addRootDomain();
			this.addAdmin();

			this.setRoles("administrator");
			this.setRoles("domainadmin");
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public Command getStatus(){
		return _status;
	}
	
	public void execute(Command command, String[] params) throws Exception{
		switch (command){
		case AddUser:
			String userName = params[1];
			String userEmail = params[2];
			String userPWD = params[3];
			this.addUser(userName, userEmail, userPWD);
			System.out.println(command.toString() + " executed");
			_status = Command.AddDomain;
			break;
			
		case AddDomain:
			String domainName = params[1];
			this.setDomain(domainName);
			System.out.println(command.toString() + " executed");
			_status = Command.BindUserToDomain;
			break;
			
		case BindUserToDomain:
			this.setBind();
			System.out.println(command.toString() + " executed");
			_status = Command.AddUser;
			break;
			
			default:
		}
	}
	
	private void addRootDomain() throws Exception{
		String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
		PreparedStatement preparedStatement = null;
		try{
			String queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
			String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
			
			String commandJSON = SystemInitializerHelpers.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);

			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			_rootDomainUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
			if(!Utils.isUUID(_rootDomainUUID)){
				throw new Exception();
				// TODO log
			}
			
		    if(!this.exists("SELECT count(*) from ratwsserver.domain where domainName = '" + rootDomainName + "'")){
				preparedStatement = _connect.prepareStatement("insert into ratwsserver.domain values (default, ?, ?)");
				preparedStatement.setString(1, rootDomainName);
				preparedStatement.setString(2, _rootDomainUUID);
				preparedStatement.executeUpdate();
				_domain = rootDomainName;
		    }
		    else{
		    	preparedStatement = _connect.prepareStatement("UPDATE domain SET domainUUID = ? WHERE domainName = ?");
		    	preparedStatement.setString(1, _rootDomainUUID);
		    	preparedStatement.setString(2, rootDomainName);
		    	
		    	preparedStatement.executeUpdate();
		    }
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void addAdmin() throws Exception{
		String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
		String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
		String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
		
		this.setUsers(userAdminName, "admin@email.com", userAdminPwd);
		this.setUsersRole(userAdminName, "administrator");
		this.setDomainRoles(rootDomainName, userAdminName, "administrator");
		this.setUserDomain(userAdminName, rootDomainName, _rootDomainUUID);
		
		this.setAdminPermissions("administrator", rootDomainName, "createcollaborationdomain");
		this.setAdminPermissions("administrator", rootDomainName, "createnewuser");
		this.setAdminPermissions("administrator", rootDomainName, "deleteuser");
		this.setAdminPermissions("administrator", rootDomainName, "deletecollaborationdomain");
		
		String commandJSON = SystemInitializerHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", _rootDomainUUID, userAdminName, userAdminPwd);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);

		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String adminUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(adminUUID)){
			throw new Exception();
			// TODO log
		}
	}
	
	private void addUser(String userName, String userEmail, String userPWD) throws Exception{
		this.setUsers(userName, userEmail, userPWD);
		this.setUsersRole(userName, "domainadmin");
		_userName = userName;

		String commandJSON = SystemInitializerHelpers.createNewUser("AddNewUser.conf", _rootDomainUUID, _userName, userPWD);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		_userUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(_userUUID)){
			throw new Exception();
			// TODO log
		}
	}

	private boolean exists(String query) throws Exception{
		PreparedStatement preparedStatement = null;
		int num = 0;
		try{
			preparedStatement = _connect.prepareStatement(query);
		    ResultSet resultSet = preparedStatement.executeQuery();
		    
		    while (resultSet.next()) {
		    	num = resultSet.getInt(1);
		    }
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
		
		return num > 0;
	}
	
	private void setDomain(String domain) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
		    if(!this.exists("SELECT count(*) from ratwsserver.domain where domainName = '" + domain + "'")){
				String commandJSON = SystemInitializerHelpers.createNewDomain("AddNewDomain.conf", _rootDomainUUID, domain);
				String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
	
				MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
				_domainUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
				if(!Utils.isUUID(_domainUUID)){
					throw new Exception();
					// TODO log
				}
				
				preparedStatement = _connect.prepareStatement("insert into ratwsserver.domain values (default, ?, ?)");
				preparedStatement.setString(1, domain);
				preparedStatement.setString(2, _domainUUID);
				preparedStatement.executeUpdate();
				_domain = domain;
				
				this.setAdminPermissions("domainadmin", _domain, "createcollaborationdomain");
				this.setAdminPermissions("domainadmin", _domain, "createnewuser");
				this.setAdminPermissions("domainadmin", _domain, "deleteuser");
				this.setAdminPermissions("domainadmin", _domain, "deletecollaborationdomain");
				this.setAdminPermissions("domainadmin", _domain, "comment");
				this.setAdminPermissions("domainadmin", _domain, "deletecomment");
				this.setAdminPermissions("domainadmin", _domain, "executeusercommands");
				this.setAdminPermissions("domainadmin", _domain, "choosedomain");
		    }
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void setBind() throws Exception{
		this.setDomainRoles(_domain, _userName, "domainadmin");
		this.setUserDomain(_userName, _domain, _domainUUID);
		
		String commandJSON = SystemInitializerHelpers.bindUserToDomain("BindFromUserToDomain.conf", _domainUUID, _userUUID);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);

		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String resultUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(resultUUID)){
			throw new Exception();
			// TODO log
		}
	}
	
	private void setDomainRoles(String domain, String userName, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.domain_role where domainName = '" + domain + "' and " +
					"userName = '" + userName + "' and roleName = '" + role + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.domain_role values (default, ?, ?, ?)");
				preparedStatement.setString(1, domain);
				preparedStatement.setString(2, userName);
				preparedStatement.setString(3, role);
				preparedStatement.executeUpdate();
			}
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void setUserDomain(String userName, String domain, String domainUUID) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user_domain where domainName = '" + domain + "' and " +
					"userName = '" + userName + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user_domain values (default, ?, ?, ?)");
				preparedStatement.setString(1, userName);
				preparedStatement.setString(2, domain);
				preparedStatement.setString(3, domainUUID);
				preparedStatement.executeUpdate();
			}
		    else{
		    	preparedStatement = _connect.prepareStatement("UPDATE user_domain SET domainUUID = ? WHERE domainName = ?");
		    	preparedStatement.setString(1, domainUUID);
		    	preparedStatement.setString(2, domain);
		    	
		    	preparedStatement.executeUpdate();
		    }
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void setUsersRole(String userName, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user_role where userName = '" + userName + "' and " +
					"roleName = '" + role + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user_role values (default, ?, ?)");
				preparedStatement.setString(1, userName);
				preparedStatement.setString(2, role);
				preparedStatement.executeUpdate();
			}
		
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void setRoles(String roleName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.role where roleName = '" + roleName + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.role values (default, ?)");
				preparedStatement.setString(1, roleName);
				preparedStatement.executeUpdate();
			}
		
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}
	
	private void setUsers(String userName, String userEmail, String userPWD) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user where userName = '" + userName + "' and " +
					"email = '" + userEmail + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user values (default, ?, ?, ?)");
				preparedStatement.setString(1, userName);
				preparedStatement.setString(2, userEmail);
				preparedStatement.setString(3, userPWD);
				preparedStatement.executeUpdate();
			}
		
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}

	private void setAdminPermissions(String roleName, String domainName, String permissionName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.permissions where roleName = '" + roleName + "' and " +
					"domainName = '" + domainName + "' and permissionName = '" + permissionName + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.permissions values (default, ?, ?, ?)");
				
				preparedStatement.setString(1, roleName);
				preparedStatement.setString(2, domainName);
				preparedStatement.setString(3, permissionName);
				preparedStatement.executeUpdate();
			}
		
		}
		catch(Exception e){
			throw new Exception(e);
		}
		finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}
	}

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		
		System.out.println("Enter 'stop' to quit.");
		InitDB initDB = new InitDB();
		try {
			initDB.openDB();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		try {
			
			do {
				String message = null;
				Command status = initDB.getStatus();
				int paramLength = 0;
				switch (status){
				case AddUser:
					message = "please enter command " + status.toString() + " <userName> <userEmail> <userPWD>";
					paramLength = 4;
					break;
					
				case AddDomain:
					message = "please enter command " + status.toString() + " <domain>";
					paramLength = 2;
					break;
					
				case BindUserToDomain:
					message = "please enter command " + status.toString();
					paramLength = 1;
					break;
					
					default:
				}
				System.out.println(message);
				str = br.readLine();
				
				if(!str.equalsIgnoreCase("stop")){
					String[] params = str.split(" ");
					if(params == null || params.length < 1){
						System.out.println("Command error!");
					}
					else{
						String command = params[0];
						if(command.equalsIgnoreCase(status.toString())){
							if(params.length < paramLength){
								System.out.println("Command parameters error!");
								continue;
							}
							
							initDB.execute(status, params);
						}
						else{
							System.out.println("Command error!");
							continue;
						}
					}
				}
				
			} while(!str.equals("stop"));
			
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(initDB != null){
				try {
					initDB.closeDB();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
