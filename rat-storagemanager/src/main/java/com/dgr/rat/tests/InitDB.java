package com.dgr.rat.tests;

import java.nio.file.FileSystems;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.Before;
import org.junit.Test;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.mqmessages.RATJSONMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rat.init.SystemInitializerTestHelpers;
import com.rat.init.User;
import com.rat.init.InitDB.Command;
import com.tinkerpop.blueprints.Vertex;

public class InitDB {
	private FileSystemXmlApplicationContext _context = null;
	private DBManager _dbManager = null;
	
	// COMMENT: QueriesTemplateUUID, CommandsTemplateUUID, RootPlatformDomainName e RootPlatformDomainUUID sono definiti
	// all'interno del file application.properties
	private String _queriesTemplateUUID = null;
	private String _commandsTemplateUUID = null;
	private String _rootDomainName = null;
	private String _rootDomainUUID = null;
	
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
		try {
			this.addAdmin();
			this.bulkCreation();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void bulkCreation() throws Exception{
		if(FileUtils.fileExists("conf" + FileSystems.getDefault().getSeparator() + "users.txt")){
			String json = FileUtils.fileRead("conf" + FileSystems.getDefault().getSeparator() + "users.txt");
			ObjectMapper mapper = new ObjectMapper();
	    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    	JsonNode jsonNode = mapper.readTree(json);
	    	Iterator <JsonNode> it = jsonNode.iterator();
	    	while(it.hasNext()){
	    		JsonNode node = it.next();
	    		User user = mapper.readValue(node.toString(), User.class);
	    		String userUUID = this.addUser(user.name, user.email, user.password);
	    		
	    		List<Vertex> result = this.getUser(VertexType.User, _rootDomainUUID, user.email, "GetUserByEmail.conf");
	    		System.out.println(result.size());
	    		Assert.assertTrue(result.size() == 1);
	    		
	    		List<String>domains = user.domains;
	    		for(String domain : domains){
	    			if(!this.domainExists(userUUID, domain)){
	    				_domainUUID = this.createNewDomain(_rootDomainUUID, domain);
	    				this.setDomain(domain);
	    			}

	    			this.setBind();
	    		}
	    		
	    		System.out.println(node.toString());
	    	}
		}
	}
	
	private String addUser(String userName, String userEmail, String userPWD) throws Exception{
		String userUUID = null;
		List<Vertex> result = this.getUser(VertexType.User, _rootDomainUUID, userEmail, "GetUserByEmail.conf");
		
		if(result.size() == 0){
			String commandJSON = SystemInitializerTestHelpers.createNewUser("AddNewUser.conf", _rootDomainUUID, userName, userPWD, userEmail);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
			
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			userUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		}
		else if(result.size() == 1){
			Vertex vertex = result.get(0);
			userUUID = vertex.getProperty(RATConstants.VertexUUIDField);
		}
		else{
			throw new Exception();
		}
		
		if(!Utils.isUUID(userUUID)){
			throw new Exception();
			// TODO log
		}
		_dbManager.addUser(userName, userEmail, userPWD, userUUID);
		_dbManager.addUserRole(userUUID, "domainadmin");
		
		return userUUID;
	}
	
	@SuppressWarnings("deprecation")
	private void addAdmin() throws Exception{
		String userName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
		String userPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
		String userEmail = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminEmail);
		String adminUUID = null;
		
		List<Vertex> result = this.getUser(VertexType.RootAdminUser, _rootDomainUUID, userEmail, "GetAdminUserByEmail.conf");
		
		if(result.size() == 0){
			String commandJSON = SystemInitializerTestHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", _rootDomainUUID, userName, userPwd, userEmail);
			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
			
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			adminUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		}
		else if(result.size() == 1){
			Vertex vertex = result.get(0);
			adminUUID = vertex.getProperty(RATConstants.VertexUUIDField);
		}
		else{
			throw new Exception();
		}
		
		if(!Utils.isUUID(adminUUID)){
			throw new Exception();
			// TODO log
		}
		
		result = this.getUser(VertexType.RootAdminUser, _rootDomainUUID, userEmail, "GetAdminUserByEmail.conf");
		Assert.assertTrue(result.size() == 1);
		
		_dbManager.addAdmin(userEmail, userName, userPwd, _rootDomainName, adminUUID);
		_dbManager.addAdminPermissions(adminUUID, _rootDomainUUID, _rootDomainName);
	}
	
	private List<Vertex> getUser(VertexType userType, String domainUUID, String userEmail, String fileName) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.createGetUserByEmail(fileName, domainUUID, userEmail);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandJSON));
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		
		RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(jsonResponse);
		System.out.println("countAllNodes: " + ratJSONMessage.countAllNodes());
		System.out.println("countNodes: " + ratJSONMessage.countNodes());
		
		List<Vertex> result = ratJSONMessage.getNode(userType, "userEmail", userEmail);
		
		return result;
	}
	
	public void init() throws Exception{
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			_context = new FileSystemXmlApplicationContext(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-producer-unitTest.xml");
			RATSessionManager.init();
			
			_queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
			_commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
			_rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
			_rootDomainUUID = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainUUID);
			
			_dbManager = new DBManager(_context);
			_dbManager.openDB();
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
	}

}
