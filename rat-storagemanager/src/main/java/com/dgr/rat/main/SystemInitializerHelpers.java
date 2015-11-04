/**
 * @author: Daniele Grignani
 * @date: Nov 1, 2015
 */

package com.dgr.rat.main;

import java.nio.file.FileSystems;

import org.junit.Assert;

import com.dgr.rat.command.graph.executor.engine.RemoteCommandsContainer;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;

public class SystemInitializerHelpers {
	public static String createRootDomain(String fileName, String commandsNodeUUID, String queriesNodeUUID) throws Exception{
		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		int changed = remoteCommandsContainer.setValue("commandsNodeUUID", commandsNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("queriesNodeUUID", queriesNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	public static String createAddRootDomainAdminUser(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd) throws Exception{
		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userName", userAdminName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", userAdminPwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	private static Response execute(String commandJSON) throws Exception{
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		
		StatusCode statusCode = StatusCode.fromString(response.getStatusCode());
		System.out.println("StatusCode " + statusCode.toString());
		if(statusCode != StatusCode.Ok){
			throw new Exception();
			// TODO: log
		}
		
		return response;
	}
	
	private static String getUUID(Response response) throws Exception{
		Object newObjectUUID = response.getCommandResponse().getProperty(RATConstants.VertexUUIDField);
		if(newObjectUUID == null){
			throw new Exception();
			// TODO: log
		}
		if(!Utils.isUUID(newObjectUUID.toString())){
			throw new Exception();
			// TODO: log
		}
		
		return newObjectUUID.toString();
	}
	
	public static String createNewUser(String fileName, String rootDomainUUID, String userName, String pwd) throws Exception{
		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("ratNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("ratNodeUUID changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		
		return commandJSON;
	}
	
	public static String createNewDomain(String fileName, String rootDomainUUID, String domainName) throws Exception{
		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexLabelField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexContentField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	public static String bindUserToDomain(String fileName, String domainUUID, String userUUID) throws Exception{
		String json = SystemInitializerHelpers.readCommandJSONFile(fileName);
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("domainUUID", domainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userUUID", userUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return commandJSON;
	}
	
	private static String readCommandJSONFile(String fileName) throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.CommandsFolder);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(commandsPath);
		pathBuffer.append(fileName);
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		
		return input;
	}
}
