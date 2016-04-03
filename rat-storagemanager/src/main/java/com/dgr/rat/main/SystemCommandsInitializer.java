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
import com.dgr.rat.command.graph.executor.engine.RemoteCommandContainer;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.StorageType;
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
		// COMMENT: modo corretto per ottenere  il commandsTemplateUUID
		//String commandsTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
//		String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
//		if(!Utils.isUUID(commandsTemplateUUID.toString())){
//			throw new Exception();
//			// TODO: log
//		}
		String commandsTemplateUUID = this.addCommandTemplates(templatesPath, json);
		
		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueries.conf");
		// COMMENT: modo corretto per ottenere  il queriesTemplateUUID
//		String queriesTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
//		String queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
//		if(!Utils.isUUID(queriesTemplateUUID.toString())){
//			throw new Exception();
//			// TODO: log
//		}
		templatesPath = RATUtils.getCommandsPath(RATConstants.QueryTemplatesFolder);
		String queriesTemplateUUID = this.addCommandTemplates(templatesPath, json);
		_storage.shutDown();

		// COMMENT: qui aprirò e chiuderò una nuova connection
		String commandJson = this.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandJson));
		this.addRootPlatformDomainNode(commandJson);
	}
//	public void addCommandTemplates() throws Exception{
//		_storage.openConnection();
//		
//		String templatesPath = RATUtils.getCommandsPath(RATConstants.CommandTemplatesFolder);
//		String json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadCommandsTemplate.conf");
//		// COMMENT: modo corretto per ottenere  il commandsTemplateUUID
//		//String commandsTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
//		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
//		String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
//		if(!Utils.isUUID(commandsTemplateUUID.toString())){
//			throw new Exception();
//			// TODO: log
//		}
//		this.addCommandTemplates(templatesPath, json, commandsTemplateUUID);
//		
//		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueriesTemplate.conf");
//		// COMMENT: modo corretto per ottenere  il queriesTemplateUUID
////		String queriesTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
//		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
//		String queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
//		if(!Utils.isUUID(queriesTemplateUUID.toString())){
//			throw new Exception();
//			// TODO: log
//		}
//		templatesPath = RATUtils.getCommandsPath(RATConstants.QueryTemplatesFolder);
//		this.addCommandTemplates(templatesPath, json, queriesTemplateUUID);
//		_storage.shutDown();
//
//		// COMMENT: qui aprirò e chiuderò una nuova connection
//		String commandJson = this.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
//		this.addRootPlatformDomainNode(commandJson);
//	}
	
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
