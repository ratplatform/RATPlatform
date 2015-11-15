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
import java.util.Iterator;
import java.util.List;
import com.dgr.utils.FileUtils;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.queries.QueryHelpers;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.main.SystemInitializerHelpers;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.AppProperties;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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
			
			String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
			String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
			String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
			
			String userEmail = "admin@email.com";
			// addAdmin(String userEmail, String userAdminName, String userAdminPwd, String rootDomainName)
			this.addAdmin(userEmail, userAdminName, userAdminPwd, rootDomainName);

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
			
//		case BulkCreation:
//			this.bulkCreation();
//			System.out.println(command.toString() + " executed");
//			_status = Command.AddUser;
//			break;
			
			default:
		}
	}
	
	public void bulkCreation() throws Exception{
		_status = Command.AddUser;
		if(FileUtils.fileExists("conf" + FileSystems.getDefault().getSeparator() + "users.txt")){
			String json = FileUtils.fileRead("conf" + FileSystems.getDefault().getSeparator() + "users.txt");
			ObjectMapper mapper = new ObjectMapper();
	    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    	JsonNode jsonNode = mapper.readTree(json);
	    	Iterator <JsonNode> it = jsonNode.iterator();
	    	while(it.hasNext()){
	    		JsonNode node = it.next();
	    		User user = mapper.readValue(node.toString(), User.class);
	    		this.addUser(user.name, user.email, user.password);
	    		
	    		List<String>domains = user.domains;
	    		for(String domain : domains){
	    			this.domainExists(_userUUID, domain);
	    			this.createNewDomain(_rootDomainUUID, domain);
	    			
	    			this.setDomain(domain);
	    			this.setBind();
	    		}
	    		
	    		System.out.println(node.toString());
	    	}
		}
	}
	
	private String createNewDomain(String rootDomainUUID, String domainName) throws Exception{
		
		
		String commandJSON = SystemInitializerHelpers.createNewDomain("AddNewDomain.conf", rootDomainUUID, domainName);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		
		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String domainUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(domainUUID)){
			throw new Exception();
			// TODO log
		}
		
		return domainUUID;
	}
	
	private String getSingleValue(String column, String param, String query) throws Exception{
		PreparedStatement preparedStatement = null;
		String result = null;
		try{
			preparedStatement = _connect.prepareStatement(query);
			preparedStatement.setString(1, param);
			
		    ResultSet resultSet = preparedStatement.executeQuery();
		    if(resultSet.next()){
		    	result = resultSet.getString(column);
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
		
		return result;
	}
	
	private void domainExists(String userUUID, String domainName) throws Exception{
		String commandJSON  = QueryHelpers.queryGetUserDomainByName("GetUserDomainByName.conf", userUUID, domainName);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
//		Response response = QueryHelpers.executeRemoteCommand(json);
//		json = JSONObjectBuilder.serializeCommandResponse(response);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
	}
	
	private void addUser(String userName, String userEmail, String userPWD) throws Exception{
		if(!this.exists("SELECT count(*) from ratwsserver.user where email = '" + userEmail + "'")){
			String commandJSON = SystemInitializerHelpers.createNewUser("AddNewUser.conf", _rootDomainUUID, userName, userPWD);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			_userUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
			if(!Utils.isUUID(_userUUID)){
				throw new Exception();
				// TODO log
			}
			
			this.setUser(userName, userEmail, userPWD, _userUUID);
			this.setUsersRole(_userUUID, "domainadmin");
			_userName = userName;
		}
		else{
			_userUUID = this.getSingleValue("userUUID", userEmail, "SELECT userUUID from ratwsserver.user where email = ?");
		}
	}
	
	private void addRootDomain() throws Exception{
		String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
		PreparedStatement preparedStatement = null;
		try{
			// COMMENT: se il RootDOmain esiste, non viene creato e viene reatituito l'UUID
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
	
	private void addAdmin(String userEmail, String userAdminName, String userAdminPwd, String rootDomainName) throws Exception{
		if(!this.exists("SELECT count(*) from ratwsserver.user where email = '" + userEmail + "'")){
			String commandJSON = SystemInitializerHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", _rootDomainUUID, userAdminName, userAdminPwd);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
	
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			String adminUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
			if(!Utils.isUUID(adminUUID)){
				throw new Exception();
				// TODO log
			}
			this.setUser(userAdminName, userEmail, userAdminPwd, adminUUID);
			this.setUsersRole(adminUUID, "administrator");
			this.setDomainRoles(_rootDomainUUID, adminUUID, "administrator");
			this.setUserDomain(adminUUID, _rootDomainUUID, rootDomainName);
			
			this.setAdminPermissions("administrator", _rootDomainUUID, "createcollaborationdomain");
			this.setAdminPermissions("administrator", _rootDomainUUID, "createnewuser");
			this.setAdminPermissions("administrator", _rootDomainUUID, "deleteuser");
			this.setAdminPermissions("administrator", _rootDomainUUID, "deletecollaborationdomain");
		}
	}
	
//	private void addUser(String userName, String userEmail, String userPWD) throws Exception{
//		if(!this.exists("SELECT count(*) from ratwsserver.user where email = '" + userEmail + "'")){
//			String commandJSON = SystemInitializerHelpers.createNewUser("AddNewUser.conf", _rootDomainUUID, _userName, userPWD);
//			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
//			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
//			_userUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
//			if(!Utils.isUUID(_userUUID)){
//				throw new Exception();
//				// TODO log
//			}
//			
//			this.setUser(userName, userEmail, userPWD, _userUUID);
//			this.setUsersRole(userName, "domainadmin");
//			_userName = userName;
//		}
//	}

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
	
	private void setDomain(String domainName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
		    if(!this.exists("SELECT count(*) from ratwsserver.domain where domainUUID = '" + domainName + "'")){
				String commandJSON = SystemInitializerHelpers.createNewDomain("AddNewDomain.conf", _rootDomainUUID, domainName);
				String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
	
				MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
				_domainUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
				if(!Utils.isUUID(_domainUUID)){
					throw new Exception();
					// TODO log
				}
				
				preparedStatement = _connect.prepareStatement("insert into ratwsserver.domain values (default, ?, ?)");
				preparedStatement.setString(1, domainName);
				preparedStatement.setString(2, _domainUUID);
				preparedStatement.executeUpdate();
				_domain = domainName;
				
				this.setAdminPermissions("domainadmin", _domainUUID, "createcollaborationdomain");
				this.setAdminPermissions("domainadmin", _domainUUID, "createnewuser");
				this.setAdminPermissions("domainadmin", _domainUUID, "deleteuser");
				this.setAdminPermissions("domainadmin", _domainUUID, "deletecollaborationdomain");
				this.setAdminPermissions("domainadmin", _domainUUID, "comment");
				this.setAdminPermissions("domainadmin", _domainUUID, "deletecomment");
				this.setAdminPermissions("domainadmin", _domainUUID, "executeusercommands");
				this.setAdminPermissions("domainadmin", _domainUUID, "choosedomain");
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
		this.setDomainRoles(_domainUUID, _userUUID, "domainadmin");
		this.setUserDomain(_userUUID, _domainUUID, _domain);
		
		String commandJSON = SystemInitializerHelpers.bindUserToDomain("BindFromUserToDomain.conf", _domainUUID, _userUUID);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);

		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String resultUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(resultUUID)){
			throw new Exception();
			// TODO log
		}
	}
	
	private void setDomainRoles(String domainUUID, String userUUID, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.domain_role where domainUUID = '" + domainUUID + "' and " +
					"userUUID = '" + userUUID + "' and roleName = '" + role + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.domain_role values (default, ?, ?, ?)");
				preparedStatement.setString(1, domainUUID);
				preparedStatement.setString(2, userUUID);
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
	
	private void setUserDomain(String userUUID, String domainUUID, String domainName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user_domain where domainUUID = '" + domainUUID + "' and " +
					"userUUID = '" + userUUID + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user_domain values (default, ?, ?, ?)");
				preparedStatement.setString(1, userUUID);
				preparedStatement.setString(2, domainUUID);
				preparedStatement.setString(3, domainName);
				preparedStatement.executeUpdate();
			}
//		    else{
//		    	preparedStatement = _connect.prepareStatement("UPDATE user_domain SET domainUUID = ? WHERE domainName = ?");
//		    	preparedStatement.setString(1, domainUUID);
//		    	preparedStatement.setString(2, domain);
//		    	
//		    	preparedStatement.executeUpdate();
//		    }
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
	
	private void setUsersRole(String userUUID, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user_role where userUUID = '" + userUUID + "' and " +
					"roleName = '" + role + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user_role values (default, ?, ?)");
				preparedStatement.setString(1, userUUID);
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
	
	private void setUser(String userName, String userEmail, String userPWD, String uuid) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.user where email = '" + userEmail + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user values (default, ?, ?, ?, ?)");
				preparedStatement.setString(1, userName);
				preparedStatement.setString(2, userEmail);
				preparedStatement.setString(3, userPWD);
				preparedStatement.setString(4, uuid);
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

	private void setAdminPermissions(String roleName, String domainUUID, String permissionName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("SELECT count(*) from ratwsserver.permissions where roleName = '" + roleName + "' and " +
					"domainUUID = '" + domainUUID + "' and permissionName = '" + permissionName + "'")){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.permissions values (default, ?, ?, ?)");
				
				preparedStatement.setString(1, roleName);
				preparedStatement.setString(2, domainUUID);
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
							if(str.equalsIgnoreCase("bulk")){
								initDB.bulkCreation();
							}
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
