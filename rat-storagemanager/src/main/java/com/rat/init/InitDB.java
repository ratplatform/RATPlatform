package com.rat.init;

import java.nio.file.FileSystems;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.mqmessages.RATJSONMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.queries.QueryHelpers;
import com.dgr.rat.json.command.parameters.SystemInitializerTestHelpers;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.StorageType;
import com.dgr.rat.tests.DBManager;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.rat.tests.TestHelpers;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	@SuppressWarnings("deprecation")
	@Before
	public void before(){
		try {
			this.init();
			
			Assert.assertNotNull(_dbManager);
			Assert.assertNotNull(_context);
			Assert.assertNotNull(_queriesTemplateUUID);
			Assert.assertNotNull(_commandsTemplateUUID);
			Assert.assertNotNull(_rootDomainName);
			Assert.assertNotNull(_rootDomainUUID);
		} 
		catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		try {
//			this.initDB();
			this.addAdmin();
			this.bulkCreation();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private void initDB() throws Exception{
//		String storageType = AppProperties.getInstance().getStringProperty(RATConstants.StorageType);
//		if(storageType.equals(StorageType.OrientDB.toString())){
//			String orientDBDataDir = AppProperties.getInstance().getStringProperty("orientdb.dir");
//			if(!FileUtils.fileExists(orientDBDataDir)){
//				FileUtils.createDir(orientDBDataDir);
//			}
//			else{
//				FileUtils.deleteDirectory(orientDBDataDir);
//				FileUtils.createDir(orientDBDataDir);
//			}
//			
//		}
//	}
	
	@After
	public void verify(){
		// Anche in caso di exception voglio che scriva lo stesso i risultati per vederli
		try {
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + RATConstants.PropertyFileName);
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String resultFilename = this.getClass().getSimpleName() + "Result";
			String path = TestHelpers.writeGraphToHTML(resultFilename, "commandResults");
			
			StorageBridge.getInstance().init(StorageType.TinkerGraph);
			TestHelpers.dumpJson(path);
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
	    		String domainUUID = null;
	    		
	    		Map<String, String>domainMap = new HashMap<String, String>();
	    		List<String>domains = user.domains;
	    		for(String domain : domains){
	    			if(!this.domainExists(userUUID, domain)){
	    				domainUUID = this.createNewDomain(_rootDomainUUID, domain);
	    				this.setBind(domainUUID, userUUID);
	    				this.domainExists(userUUID, domain);
	    			}
	    			else{
	    				result = this.getDomain(userUUID, domain);
	    	    		System.out.println(result.size());
	    	    		Assert.assertTrue(result.size() == 1);
	    	    		domainUUID = result.get(0).getProperty(RATConstants.VertexUUIDField);
	    			}
	    			domainMap.put(domain, domainUUID);
	    			
    				_dbManager.addDomain(domainUUID, domain);
    				_dbManager.setDomainRoles(domainUUID, userUUID, "domainadmin");
    				_dbManager.setUserDomain(userUUID, domainUUID, domain);
	    		}
	    		
	    		Comments comments = mapper.readValue(node.toString(), Comments.class);
	    		List<Comment>commentList = comments.comments;
	    		if(commentList != null){
		    		for(Comment comment : commentList){
		    			domainUUID = domainMap.get(comment.domain);
		    			System.out.println(domainUUID);
		    			this.addComment(comment, domainUUID, userUUID);
		    		}
	    		}
	    		
	    		System.out.println(node.toString());
	    	}
		}
	}
	
	private void addComment(Comment comment, String domainUUID, String userUUID) throws Exception{
//		String commandJSON = SystemInitializerTestHelpers.addUserComment("AddComment.conf", 
//				userUUID, domainUUID, -1, -1, -1, -1, comment.url, comment.VertexContentField, comment.VertexLabelField);
		String commandJSON = SystemInitializerTestHelpers.addUserComment("AddComment.conf", 
				domainUUID, userUUID, "{json coordinates}", comment.url, comment.VertexContentField, comment.VertexLabelField);
		
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String status = message.getHeader().getStatusCode();
		Assert.assertEquals("200", status);
	}
	
	private void setBind(String domainUUID, String userUUID) throws Exception{
		
		String commandJSON = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", domainUUID, userUUID);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);

		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String resultUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(resultUUID)){
			throw new Exception();
			// TODO log
		}
	}
	
	private String createNewDomain(String rootDomainUUID, String domainName) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.createNewDomain("AddNewDomain.conf", rootDomainUUID, domainName);
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
	
	private boolean domainExists(String userUUID, String domainName) throws Exception{
		List<Vertex> result = this.getDomain(userUUID, domainName);
		
		return result.size() > 0 ? true : false;
	}
	
	private List<Vertex> getDomain(String userUUID, String domainName) throws Exception{
		String commandJSON  = SystemInitializerTestHelpers.createGetUserDomainByName("GetUserDomainByName.conf", userUUID, domainName, VertexType.Domain);
		String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		
		RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(jsonResponse);
		System.out.println("countAllNodes: " + ratJSONMessage.countAllNodes());
		System.out.println("countNodes: " + ratJSONMessage.countNodes());
		
		List<Vertex> result = ratJSONMessage.getNode(VertexType.Domain, "domainName", domainName);
		
		return result;
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
		Assert.assertNotNull(_context);
		
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
		String commandJSON = SystemInitializerTestHelpers.createGetUserByEmail(fileName, domainUUID, "userEmail", userEmail);
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
			e.printStackTrace();
			throw new Exception(e);
		}
	}

}
