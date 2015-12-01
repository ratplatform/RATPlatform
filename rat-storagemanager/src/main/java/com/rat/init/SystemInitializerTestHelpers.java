/**
 * @author: Daniele Grignani
 * @date: Nov 1, 2015
 */

package com.rat.init;

import org.junit.Assert;
import com.dgr.rat.command.graph.executor.engine.RemoteCommandsContainer;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class SystemInitializerTestHelpers {
	public static String createAddRootDomainAdminUser(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd, String email) throws Exception{
		String json = RATHelpers.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userName", userAdminName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", userAdminPwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userEmail", email, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUserByEmail(String fileName, String rootDomainUUID, String paramName, String paramValue) throws Exception{
		String json = RATHelpers.readQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(paramName, paramValue, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUsersAndDomains(String fileName, String rootDomainUUID, VertexType paramValue) throws Exception{
		String json = RATHelpers.readQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(RATConstants.VertexTypeField, paramValue.toString(), ReturnType.string);
		System.out.println(RATConstants.VertexTypeField + " changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetUserDomainByName(String fileName, String rootDomainUUID, String domainName, VertexType paramValue) throws Exception{
		String json = RATHelpers.readQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue(RATConstants.VertexTypeField, paramValue.toString(), ReturnType.string);
		System.out.println(RATConstants.VertexTypeField + " changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("domainName", domainName, ReturnType.string);
		System.out.println("domainName changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createGetAllNodesByType(String fileName, String rootDomainUUID) throws Exception{
		String json = RATHelpers.readQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("rootNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createNewUser(String fileName, String rootDomainUUID, String userName, String pwd, String email) throws Exception{
		String json = RATHelpers.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("isPutByNode2UUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isPutByNode2UUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("isPutByNodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userEmail", email, ReturnType.string);
		System.out.println("userEmail changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATJsonUtils.getRATJson(jsonHeader);
		
		return newJson;
	}
	
	public static String createNewDomain(String fileName, String rootDomainUUID, String domainName) throws Exception{
		String json = RATHelpers.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexLabelField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
//		changed = remoteCommandsContainer.setValue("VertexContentField", domainName, ReturnType.string);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("domainName", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
//	private static Response execute(String commandJSON) throws Exception{
//		CommandSink commandSink = new CommandSink();
//		Response response = commandSink.doCommand(commandJSON);
//		
//		StatusCode statusCode = StatusCode.fromString(response.getStatusCode());
//		System.out.println("StatusCode " + statusCode.toString());
//		if(statusCode != StatusCode.Ok){
//			throw new Exception();
//			// TODO: log
//		}
//		
//		return response;
//	}
//	
//	private static String getUUID(Response response) throws Exception{
//		Object newObjectUUID = response.getCommandResponse().getProperty(RATConstants.VertexUUIDField);
//		if(newObjectUUID == null){
//			throw new Exception();
//			// TODO: log
//		}
//		if(!Utils.isUUID(newObjectUUID.toString())){
//			throw new Exception();
//			// TODO: log
//		}
//		
//		return newObjectUUID.toString();
//	}
	
//	public static String createNewUserA(String fileName, String rootDomainUUID, String userName, String pwd) throws Exception{
//		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
//		
//		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
//		
//		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
//		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
//		
//		int changed = remoteCommandsContainer.setValue("ratNodeUUID", rootDomainUUID, ReturnType.uuid);
//		System.out.println("ratNodeUUID changed in " + fileName + ": " + changed);
//		
//		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
//		System.out.println("userName changed in " + fileName + ": " + changed);
//		
//		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
//		System.out.println("userPwd changed in " + fileName + ": " + changed);
//		
//		jsonHeader.setSettings(remoteCommandsContainer.serialize());
//		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		
//		return commandJSON;
//	}
	
	public static String bindUserToDomain(String fileName, String domainUUID, String userUUID) throws Exception{
		String json = RATHelpers.readCommandJSONFile(fileName);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
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
}
