/**
 * @author Daniele Grignani (dgr)
 * @date Aug 19, 2015
 */

package com.dgr.rat.main;

import java.io.File;
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
		
		String templatesPath = RATHelpers.getCommandsPath(RATConstants.CommandTemplatesFolder);
		String json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadCommandsTemplate.conf");
		// COMMENT: modo corretto per ottenere  il commandsTemplateUUID
		//String commandsTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
		String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
		if(!Utils.isUUID(commandsTemplateUUID.toString())){
			throw new Exception();
			// TODO: log
		}
		this.addCommandTemplates(templatesPath, json, commandsTemplateUUID);
		
		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueriesTemplate.conf");
		// COMMENT: modo corretto per ottenere  il queriesTemplateUUID
//		String queriesTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
		String queriesTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.QueriesTemplateUUID);
		if(!Utils.isUUID(queriesTemplateUUID.toString())){
			throw new Exception();
			// TODO: log
		}
		templatesPath = RATHelpers.getCommandsPath(RATConstants.QueryTemplatesFolder);
		this.addCommandTemplates(templatesPath, json, queriesTemplateUUID);
		_storage.shutDown();

		// COMMENT: qui aprirò e chiuderò una nuova connection
		String commandJson = this.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
		this.addRootPlatformDomainNode(commandJson);
	}
	
	private String createRootDomain(String fileName, String commandsNodeUUID, String queriesNodeUUID) throws Exception{
		String json = RATHelpers.readCommandJSONFile(fileName);
		
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
	
	private void addCommandTemplates(String commandTemplatesPath, String json, String commandUUID) throws Exception{
		// COMMENT: Leggo il file contenente i comandi
		try{
			Graph graph = RATHelpers.fromRatJsonToGraph(json); 
					
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
				uuid = jsonObject.getHeaderProperty(RATConstants.CommandGraphUUID);
				
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
		}
		catch(Exception e){
			e.printStackTrace();
			_storage.rollBack();
		}
	}
}
