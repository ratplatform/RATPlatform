/**
 * @author Daniele Grignani (dgr)
 * @date Aug 19, 2015
 */

package com.dgr.rat.main;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;

import com.dgr.rat.command.graph.executor.engine.RemoteCommandContainer;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.command.parameters.SystemInitializerTestHelpers;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.StorageType;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class SystemCommandsInitializer {
	private IStorage _storage = null;
	private String _storageType = null;
	
	public SystemCommandsInitializer() {
		// TODO Auto-generated constructor stub
	}
	
	public void set_storageType(String storageType){
		this._storageType = storageType;
	}
	
	public void initStorage() throws Exception{
		StorageType storageType = StorageType.fromString(_storageType);
		StorageBridge.getInstance().init(storageType);
		_storage = StorageBridge.getInstance().getStorage();
	}
	
	// TODO: così non va bene ed è temporaneo: devo trovare il modo per eseguirlo come tutti gli altri comandi.
	public void addCommandTemplates() throws Exception{
		_storage.openConnection();
		
		String templatesPath = RATUtils.getCommandsPath(RATConstants.CommandTemplatesFolder);
		String json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadCommands.conf");
		String commandsTemplateUUID = this.addCommandTemplates(templatesPath, json);
		
		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueries.conf");
		templatesPath = RATUtils.getCommandsPath(RATConstants.QueryTemplatesFolder);
		String queriesTemplateUUID = this.addCommandTemplates(templatesPath, json);
		_storage.shutDown();

		// COMMENT: qui aprirò e chiuderò una nuova connection
		String commandJson = this.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandJson));
		Response response = this.addRootPlatformDomainNode(commandJson);
		
		UUID rootDomainUUID = (UUID) response.getCommandResponse().getProperty(RATConstants.VertexUUIDField);
		this.addAdmin(rootDomainUUID);
	}
	
	private Response addRootPlatformDomainNode(String json)throws Exception{
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String rootDomain = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
		String input = json.replace(placeHolder, rootDomain);
		
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(input);
		
		StatusCode statusCode = StatusCode.fromString(response.getStatusCode());
		System.out.println("StatusCode " + statusCode.toString());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandResponse.getResponse()));

		return response;
	}
	
	private void addAdmin(UUID rootDomainUUID) throws Exception{
		String userName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
		String userPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
		String userEmail = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminEmail);
//		String adminUUID = null;
		
		String commandJSON = this.createAddRootDomainAdminUserCommand("AddRootDomainAdminUser.conf", rootDomainUUID.toString(), userName, userPwd, userEmail);
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		StatusCode statusCode = StatusCode.fromString(response.getStatusCode());
		System.out.println("StatusCode " + statusCode.toString());
	}
	
	private String createRootDomain(String fileName, String commandsNodeUUID, String queriesNodeUUID) throws Exception{
		String json = RATUtils.readCommandJSONFile(fileName, RATConstants.CommandsFolder);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
		
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
		RemoteCommandContainer remoteCommandsContainer = new RemoteCommandContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		int changed = remoteCommandsContainer.setValue("commandsNodeUUID", commandsNodeUUID, ReturnType.uuid);
		//System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("queriesNodeUUID", queriesNodeUUID, ReturnType.uuid);
		//System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATJsonUtils.getRATJson(jsonHeader);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandJSON));
		
		return commandJSON;
	}
	
	private String createAddRootDomainAdminUserCommand(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd, String email) throws Exception{
		String json = RATUtils.readCommandJSONFile(fileName, RATConstants.CommandsFolder);
		
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
	
	private String addCommandTemplates(String commandTemplatesPath, String json/*, String commandUUID*/) throws Exception{
		// COMMENT: Leggo il file contenente i comandi
		String commandUUID = null;
		try{
			Graph graph = RATHelpers.fromRatJsonToGraph(json); 
			//System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			// COMMENT: this code was used because the root nodes of LoadCommands and LoadQueries were written in the properties file; now
			// with GremlinPipeline query is useless (and it does not not function)
//			Iterable<Vertex> iterable = graph.getVertices(RATConstants.VertexUUIDField, commandUUID);
//			if(iterable == null){
//				throw new Exception();
//				// TODO: log
//			}
//			Iterator<Vertex> it = iterable.iterator();
//			if(!it.hasNext()){
//				throw new Exception();
//				// TODO: log
//			}
//			Vertex commandsVertex = null;
//			Vertex inMemoryVertex = it.next();
			
			Vertex commandsVertex = null;
			Vertex inMemoryVertex = null;
			
			GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(graph.getVertices());
			inMemoryVertex = (Vertex) queryPipe.has(RATConstants.VertexIsRootField, true).next();
			
			commandUUID = inMemoryVertex.getProperty(RATConstants.VertexUUIDField);
			// TODO: attenzione: io do per scontato che sia una UUID: prima andrebbe fatto un controllo....
			if (!_storage.vertexExists(UUID.fromString(commandUUID))){
				commandsVertex = _storage.addVertex(UUID.fromString(commandUUID));
			}
			else{
				commandsVertex = _storage.getVertex(UUID.fromString(commandUUID));
			}
			
			// COMMENT: Per ora prendo direttamente gli attributi, poi vediamo
			Set<String> keys = inMemoryVertex.getPropertyKeys();
			Iterator<String> keysIt = keys.iterator();
			while(keysIt.hasNext()){
				String propertyName = keysIt.next();
				if(!propertyName.equalsIgnoreCase(RATConstants.VertexUUIDField)){
					Object propertyValue = inMemoryVertex.getProperty(propertyName);
					commandsVertex.setProperty(propertyName, propertyValue);
				}
			}
			
			String commandNameInMemoryVertex = inMemoryVertex.getProperty(RATConstants.VertexContentField);
			// COMMENT: Ed eseguo secco il comando che possiede, ossia caricare i file dei template e linkarli
			// al nodo
			// TODO: valutare l'ipotesi di modificare il comportamento e rendere il comando un grafo linkato
			// al vertice commandsVertex, e non come ora salvare il contenuto del JSON dentro il nodo del
			// comando
			ObjectMapper mapper = new ObjectMapper();
			File[] templates = FileUtils.listingFiles(false, commandTemplatesPath, ".conf");
			for(File template : templates){
				String pathFileName = template.getAbsolutePath();
	//			System.out.println(pathFileName);
				json = FileUtils.fileRead(pathFileName);
				RATJsonObject jsonObject = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
				String commandName = jsonObject.getHeaderProperty(RATConstants.CommandName);
				String commandGraphUUID = jsonObject.getHeaderProperty(RATConstants.CommandGraphUUID);
				
				if(!_storage.vertexExists(UUID.fromString(commandGraphUUID))){
					Vertex command = _storage.addVertex(UUID.fromString(commandGraphUUID));
					command.setProperty(RATConstants.CommandName, commandName);
					command.setProperty(RATConstants.VertexContentField, json);
					command.setProperty(RATConstants.VertexLabelField, commandName);
					commandsVertex.addEdge(commandNameInMemoryVertex, command);
				}
			}
			try{
			_storage.commit();
			}
			catch(Exception e){
				e.printStackTrace();
				_storage.rollBack();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			_storage.rollBack();
		}
		
		return commandUUID;
	}
}
