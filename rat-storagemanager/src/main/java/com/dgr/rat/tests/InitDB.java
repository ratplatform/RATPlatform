package com.dgr.rat.tests;

import java.nio.file.FileSystems;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.Before;
import org.junit.Test;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.mqmessages.RATJSONMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;
import com.dgr.utils.Utils;
import com.rat.init.SystemInitializerTestHelpers;

public class InitDB {
	private FileSystemXmlApplicationContext _context = null;
	
	public InitDB() {
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void before(){
		try {
			this.init();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		// COMMENT: QueriesTemplateUUID, CommandsTemplateUUID, RootPlatformDomainName e RootPlatformDomainUUID sono definiti
		// all'interno del file application.properties
		String queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
		String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
		String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
		String rootDomainUUID = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainUUID);
		
		try {
			this.addAdmin(rootDomainUUID);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addAdmin(String domainUUID) throws Exception{
		String userName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
		String userPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
		String userEmail = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminEmail);
		
		if(!this.userExists(domainUUID, userEmail, "GetAdminUsersByEmail.conf")){
			String commandJSON = SystemInitializerTestHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", domainUUID, userName, userPwd, userEmail);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
			
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			String adminUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
			if(!Utils.isUUID(adminUUID)){
				throw new Exception();
				// TODO log
			}
			
			this.userExists(domainUUID, userEmail, "GetAdminUsersByEmail.conf");
		}
	}
	
	private boolean userExists(String domainUUID, String userEmail, String fileName) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.createGetUserByEmail(fileName, domainUUID, userEmail);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		
		RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(jsonResponse);
		System.out.println("countAllNodes: " + ratJSONMessage.countAllNodes());
		System.out.println("countNodes: " + ratJSONMessage.countNodes());
//		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
//		String adminUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
//		if(!Utils.isUUID(adminUUID)){
//			throw new Exception();
//			// TODO log
//		}
		
		return ratJSONMessage.countAllNodes() > 1 ? true : false;
	}
	
	public void init() throws Exception{
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			_context = new FileSystemXmlApplicationContext(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-producer-unitTest.xml");
			RATSessionManager.init();
			
//			Class.forName("com.mysql.jdbc.Driver");
//			_connect = DriverManager.getConnection("jdbc:mysql://localhost/ratwsserver?" + "user=admin&password=admin");
//			
//			this.addRootDomain();
//			
//			String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
//			String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
//			String rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
//			
//			String userEmail = "admin@email.com";
//			// addAdmin(String userEmail, String userAdminName, String userAdminPwd, String rootDomainName)
//			this.addAdmin(userEmail, userAdminName, userAdminPwd, rootDomainName);
//
//			this.setRoles("administrator");
//			this.setRoles("domainadmin");
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
	}

}
