/**
 * @author Daniele Grignani (dgr)
 * @date Aug 19, 2015
 */

package com.dgr.rat.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Future;
import com.dgr.rat.async.dispatcher.EventManager;
import com.dgr.rat.async.dispatcher.Task;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.main.init.db.InitRATDomainTask;
import com.dgr.rat.messages.MQMessage;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.StorageType;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;

public class SystemCommandsInitializer {
	private Map<String, String> _componentMap = new HashMap<String, String>();
	private Map<String, String> _userCommandToRead = new HashMap<String, String>();
	private IStorage _storage = null;
	private String[] _executionOrder = null;
	private String _storageType = null;
	
	public SystemCommandsInitializer() {
		// TODO Auto-generated constructor stub
	}
	
	public void set_storageType(String storageType){
		this._storageType = storageType;
	}
	
	public void set_componentMap(Map<String, String> map){
		this._componentMap = map;
	}
	
	public void set_userCommandToRead(Map<String, String> map){
		this._userCommandToRead = map;
	}
	
	public void set_executionOrder(String[] executionOrder) {
		this._executionOrder = executionOrder;
	}
	
	public void initStorage() throws Exception{
		StorageType storageType = StorageType.fromString(_storageType);
		StorageBridge.getInstance().init(storageType);
		_storage = StorageBridge.getInstance().getStorage();
	}
	
	// TODO: così non va bene ed è temporaneo: devo trovare il modo per eseguirlo come tutti gli altri
	public void loadCommandTemplates() throws Exception{
		String templatesPath = RATHelpers.getCommandsPath(RATConstants.CommandTemplatesFolder);
		String json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadCommandsTemplate.conf");
		this.loadCommandTemplates(templatesPath, json, "42fc1097-b340-41a1-8ab2-e4d718ad48b9");
		
		json = FileUtils.fileRead(templatesPath + FileSystems.getDefault().getSeparator() + "LoadQueriesTemplate.conf");
		templatesPath = RATHelpers.getCommandsPath(RATConstants.QueryTemplatesFolder);
		this.loadCommandTemplates(templatesPath, json, "309c67c5-fd6e-4124-8cb6-8a455de32233");
	}
	
	private void loadCommandTemplates(String commandTemplatesPath, String json, String commandUUID) throws Exception{
		// Leggo il file coi comandi
//		String json = FileUtils.fileRead(commandTemplatesPath + FileSystems.getDefault().getSeparator() + commandFileName);
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
				// TODO: exception
			}
			
			Iterator<Vertex> it = iterable.iterator();
			if(!it.hasNext()){
				// TODO: exception
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
	
	public void runSystemCommands() throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.CommandsFolder);
		
		for(String key :_executionOrder){
			String fileName = _componentMap.get(key);
			
			StringBuffer pathBuffer = new StringBuffer();
			pathBuffer.append(commandsPath);
			pathBuffer.append(fileName);
			
			String templatePath = pathBuffer.toString();
			String input = FileUtils.fileRead(templatePath);
			
			String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
			String rootDomain = AppProperties.getInstance().getStringProperty(RATConstants.RootDomain);
			input = input.replace(placeHolder, rootDomain);
			
			InitRATDomainTask setRATDomainTask = new InitRATDomainTask(input);
			Task<MQMessage> task = new Task<MQMessage>(setRATDomainTask);
			Future<MQMessage> result = EventManager.getInstance().addTask(task);
			MQMessage resultMessage = result.get();
			
			System.out.println("SetRATDomainTask result: " + resultMessage.getResponseMessage().toString());
		}
	}
	
	// ATTENZIONE: è importante che vengano eseguita DOPO runSystemCommands
	public void readUserCommandsCommandTemplates() throws Exception{
		Iterator<String> it = _userCommandToRead.keySet().iterator();
		while(it.hasNext()){
			String commandName = it.next();
			String commandFile = _userCommandToRead.get(commandName);
			
			StringBuffer pathBuffer = new StringBuffer();
			pathBuffer.append(RATHelpers.getCommandsPath(RATConstants.CommandTemplatesFolder));
			pathBuffer.append(commandFile);
			
			String templatePath = pathBuffer.toString();
			String input = FileUtils.fileRead(templatePath);
//			input = this.setRootDomain(input);
			
			UUID uuid = _storage.getRootDomainUUID();
			if(uuid == null){
				// TODO da gestire
				throw new Exception();
			}
			String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.RootDomainUUIDPlaceholder);
			input = input.replace(placeHolder, uuid.toString());
			
			InitRATDomainTask initRATDomainTask = new InitRATDomainTask(input);
			Task<MQMessage> task = new Task<MQMessage>(initRATDomainTask);
			Future<MQMessage> result = EventManager.getInstance().addTask(task);
			MQMessage resultMessage = result.get();
			
			System.out.println("SetRATDomainTask result: " + resultMessage.getResponseMessage().toString());
		}
	}
}
