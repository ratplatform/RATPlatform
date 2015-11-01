/**
 * @author Daniele Grignani (dgr)
 * @date Aug 19, 2015
 */

package com.dgr.rat.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.RemoteCommandsContainer;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.StorageType;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;

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
	public void loadCommandTemplates() throws Exception{
		String templatesPath = RATHelpers.getCommandsPath(RATConstants.CommandTemplatesFolder);
		String json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadCommandsTemplate.conf");
		String commandsTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		this.loadCommandTemplates(templatesPath, json, commandsTemplateUUID);
		
		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueriesTemplate.conf");
		String queriesTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		templatesPath = RATHelpers.getCommandsPath(RATConstants.QueryTemplatesFolder);
		this.loadCommandTemplates(templatesPath, json, queriesTemplateUUID);
		
		// TODO: per ora eseguo tutto in modo sincrono, in seconda battuta vedremo. Questa parte è tutta da rivedere in quanto
		// i nomi dei parametri sono contenuti nelle due funzioni che seguono e così non va bene.
		String rootDomainUUID = this.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
		
		String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
		String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
		this.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", rootDomainUUID, userAdminName, userAdminPwd);
	}
	
	private void loadCommandTemplates(String commandTemplatesPath, String json, String commandUUID) throws Exception{
		// Leggo il file coi comandi
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
			String output = mapper.writeValueAsString(jsonHeader.getSettings());
			JsonNode actualObj = mapper.readTree(output);
			Graph graph = new TinkerGraph();
			InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
			GraphSONReader.inputGraph(graph, inputStream);
	
			Iterable<Vertex> iterable = graph.getVertices(RATConstants.VertexUUIDField, commandUUID);
			if(iterable == null){
				throw new Exception();
				// TODO: log
			}
			
			Iterator<Vertex> it = iterable.iterator();
			if(!it.hasNext()){
				throw new Exception();
				// TODO: log
			}
			
			Vertex commandsVertex = null;
			Vertex inMemoryVertex = it.next();
			String uuid = inMemoryVertex.getProperty(RATConstants.VertexUUIDField);
			// TODO: attenzione: io do per scontato che sia una UUID: prima andrebbe fatto un controllo....
			if (!_storage.vertexExists(UUID.fromString(uuid))){
				commandsVertex = _storage.addVertex(UUID.fromString(uuid));
			}
			else{
				commandsVertex = _storage.getVertex(UUID.fromString(uuid));
			}
			
			// Per ora prendo secco gli attributi, poi vediamo
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
			// Ed eseguo secco il comando che possiede, ossia caricare i file dei template e linkarli
			// al nodo
			// TODO: valutare l'ipotesi di modificare il comportamento e rendere il comando un grafo linkato
			// al vertice commandsVertex, e non come ora salvare il contenuto del JSON dentro il nodo del
			// comando
			File[] templates = FileUtils.listingFiles(false, commandTemplatesPath, ".conf");
			for(File template : templates){
				String pathFileName = template.getAbsolutePath();
	//			System.out.println(pathFileName);
				json = FileUtils.fileRead(pathFileName);
				jsonHeader = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
				String commandName = jsonHeader.getHeaderProperty(RATConstants.CommandName);
				uuid = jsonHeader.getHeaderProperty(RATConstants.CommandGraphUUID);
				
				if(!_storage.vertexExists(UUID.fromString(uuid))){
					Vertex command = _storage.addVertex(UUID.fromString(uuid));
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
//			_storage.shutDown();
		}
		catch(Exception e){
			e.printStackTrace();
			_storage.rollBack();
		}
	}
	
	private Response execute(String commandJSON) throws Exception{
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
	
	private void createAddRootDomainAdminUser(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd) throws Exception{
		String json = this.readCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userName", userAdminName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", userAdminPwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATHelpers.getRATJson(jsonHeader);
		
		Response response = this.execute(commandJSON);
		Object newRootUUID = response.getCommandResponse().getProperty(RATConstants.VertexUUIDField);
		if(newRootUUID == null){
			throw new Exception();
			// TODO: log
		}
		if(!Utils.isUUID(newRootUUID.toString())){
			throw new Exception();
			// TODO: log
		}
	}
	
	private String createRootDomain(String fileName, String commandsNodeUUID, String queriesNodeUUID) throws Exception{
		String json = this.readCommandJSONFile(fileName);
		
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		int changed = remoteCommandsContainer.setValue("commandsNodeUUID", commandsNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		changed = remoteCommandsContainer.setValue("queriesNodeUUID", queriesNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String commandJSON = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		Response response = this.execute(commandJSON);
		Object newRootUUID = response.getCommandResponse().getProperty(RATConstants.VertexUUIDField);
		if(newRootUUID == null){
			throw new Exception();
			// TODO: log
		}
		if(!Utils.isUUID(newRootUUID.toString())){
			throw new Exception();
			// TODO: log
		}
		
		return newRootUUID.toString();
	}
	
	private String readCommandJSONFile(String fileName) throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.CommandsFolder);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(commandsPath);
		pathBuffer.append(fileName);
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		
		return input;
	}
}
