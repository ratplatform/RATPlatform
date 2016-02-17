/**
 * @author: Daniele Grignani
 * @date: Nov 1, 2015
 */

package com.dgr.rat.json.command.parameters;

//import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class SystemInitializerTestHelpers {
	private static Map<String, String>_jsonCommandsMap = new HashMap<String, String>();
	
	private static String getJson(String fileName, String folderName) throws Exception{
		String result = null;
		if(_jsonCommandsMap.containsKey(fileName)){
			result = _jsonCommandsMap.get(fileName);
		}
		else{
			result = RATUtils.readCommandJSONFile(fileName, folderName);
			_jsonCommandsMap.put(fileName, result);
		}
		
		return result;
	}
	
	public static String createAddRootDomainAdminUser(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd, String email) throws Exception{
//		String json = RATUtils.readCommandJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.CommandsFolder);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userName", userAdminName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", userAdminPwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userEmail", email, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUserByEmail(String fileName, String rootDomainUUID, String paramName, String paramValue) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(paramName, paramValue, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUsersAndDomains(String fileName, String rootDomainUUID, VertexType paramValue) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(RATConstants.VertexTypeField, paramValue.toString(), ReturnType.string);
		System.out.println(RATConstants.VertexTypeField + " changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUserDomainByName(String fileName, String rootDomainUUID, String domainName, VertexType paramValue) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(RATConstants.VertexTypeField, paramValue.toString(), ReturnType.string);
		System.out.println(RATConstants.VertexTypeField + " changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("domainName", domainName, ReturnType.string);
		System.out.println("domainName changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetAllNodesByType(String fileName, String rootDomainUUID) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createNewUser(String fileName, String rootDomainUUID, String userName, String pwd, String email) throws Exception{
//		String json = RATUtils.readCommandJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.CommandsFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("isPutByNode2UUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isPutByNode2UUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isPutByNodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userEmail", email, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATJsonUtils.getRATJson(jsonHeader);
		
		return newJson;
	}
	
	public static String createNewDomain(String fileName, String rootDomainUUID, String domainName) throws Exception{
//		String json = RATUtils.readCommandJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.CommandsFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexLabelField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
//		changed = remoteCommandsContainer.setValue("VertexContentField", domainName, ReturnType.string);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("domainName", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	public static String bindUserToDomain(String fileName, String domainUUID, String userUUID) throws Exception{
//		String json = RATUtils.readCommandJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.CommandsFolder);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("domainNodeUUID", domainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userUUID", userUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	public static String addUserComment(String fileName, String ownerNodeUUID, String userNodeUUID, 
			String jsonCoordinates, String url, String vertexContentField, String vertexLabelField) throws Exception{
//		String json = RATUtils.readCommandJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.CommandsFolder);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("ownerNodeUUID", ownerNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userNodeUUID", userNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("jsonCoordinates", jsonCoordinates, ReturnType.string);
		System.out.println("startPageX Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("url", url, ReturnType.url);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("VertexContentField", vertexContentField, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("VertexLabelField", vertexLabelField, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	public static String createGetAllDomainComments(String fileName, String rootDomainUUID, String userUUID, String url) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		// TODO ho messo string come tipo perché AbstractCommand.setQueryPivot accetta parametri ma non i tipi dei parametri: è da correggere
		changed = remoteCommandsContainer.setValue("userUUID", userUUID, ReturnType.string);
		System.out.println("userUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("url", url, ReturnType.string);
		System.out.println("url changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetAllUserComments(String fileName, String userUUID) throws Exception{
//		String json = RATUtils.readQueryJSONFile(fileName);
		String json = SystemInitializerTestHelpers.getJson(fileName, RATConstants.QueriesFolder);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", userUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		//Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
}
