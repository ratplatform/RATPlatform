package com.dgr.rat.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBManager {
	private Connection _connect = null;
//	private FileSystemXmlApplicationContext _context = null;
	
	public DBManager() {
//		_context = context;
	}
	
	public void addDomain(String domainUUID, String domainName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("domain",  "domainUUID", domainUUID)){
				preparedStatement = _connect.prepareStatement("insert into ratwsserver.domain values (default, ?, ?)");
				preparedStatement.setString(1, domainName);
				preparedStatement.setString(2, domainUUID);
				preparedStatement.executeUpdate();
				
				this.setAdminPermissions("domainadmin", domainUUID, "createcollaborationdomain");
				this.setAdminPermissions("domainadmin", domainUUID, "createnewuser");
				this.setAdminPermissions("domainadmin", domainUUID, "deleteuser");
				this.setAdminPermissions("domainadmin", domainUUID, "deletecollaborationdomain");
				this.setAdminPermissions("domainadmin", domainUUID, "comment");
				this.setAdminPermissions("domainadmin", domainUUID, "deletecomment");
				this.setAdminPermissions("domainadmin", domainUUID, "executeusercommands");
				this.setAdminPermissions("domainadmin", domainUUID, "choosedomain");
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
	
	public void openDB() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_connect = DriverManager.getConnection("jdbc:mysql://localhost/ratwsserver?" + "user=admin&password=admin");
			
			this.setRoles("administrator");
			this.setRoles("domainadmin");
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public void closeDB() throws Exception{
		if(_connect != null){
			_connect.close();
		}
		
//		_context.close();
	}
	
	public void addAdmin(String userEmail, String userAdminName, String userAdminPwd, String rootDomainName, String adminUUID) throws Exception{
		if(!this.exists("user",  "email", userEmail)){
			this.addUser(userAdminName, userEmail, userAdminPwd, adminUUID);
			this.addUserRole(adminUUID, "administrator");
		}
	}
	
	public void addAdminPermissions(String adminUUID, String rootDomainUUID, String rootDomainName) throws Exception{
		this.setDomainRoles(rootDomainUUID, adminUUID, "administrator");
		this.setUserDomain(adminUUID, rootDomainUUID, rootDomainName);
		this.setAdminPermissions("administrator", rootDomainUUID, "createcollaborationdomain");
		this.setAdminPermissions("administrator", rootDomainUUID, "createnewuser");
		this.setAdminPermissions("administrator", rootDomainUUID, "deleteuser");
		this.setAdminPermissions("administrator", rootDomainUUID, "deletecollaborationdomain");
	}
	
	private void setRoles(String roleName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("role",  "roleName", roleName)){
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
	
	private void setAdminPermissions(String roleName, String domainUUID, String permissionName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("permissions",  "roleName", roleName) && 
					!this.exists("permissions",  "domainUUID", domainUUID) && 
					!this.exists("permissions",  "permissionName", permissionName)){
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
	
	public void setUserDomain(String userUUID, String domainUUID, String domainName) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("user_domain",  "domainUUID", domainUUID)){
				preparedStatement = _connect.prepareStatement("insert into  ratwsserver.user_domain values (default, ?, ?, ?)");
				preparedStatement.setString(1, userUUID);
				preparedStatement.setString(2, domainUUID);
				preparedStatement.setString(3, domainName);
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
	
	public void setDomainRoles(String domainUUID, String userUUID, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("domain_role",  "domainUUID", domainUUID)){
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
	
	public void addUser(String userName, String userEmail, String userPWD, String uuid) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("user",  "email", userEmail)){
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
	
	public void addUserRole(String userUUID, String role) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			if(!this.exists("user_role",  "userUUID", userUUID)){
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
	
	private boolean exists(String table, String where, String equalTo) throws Exception{
		String query = "SELECT count(*) from ratwsserver." + table + " where " + where + " = '" + equalTo + "'";
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

}
