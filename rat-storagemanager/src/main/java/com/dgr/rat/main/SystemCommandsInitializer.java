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
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
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
		// COMMENT: modo corretto per ottenere  il commandsTemplateUUID
		//String commandsTemplateUUID = RATHelpers.getRATJsonHeaderProperty(json, RATConstants.RootVertexUUID);
		// COMMENT: tuttavia in questa fase lo inserisco nel file properties in modo da poterlo usare anche in InitDB (classe temporanea)
		String commandsTemplateUUID = AppProperties.getInstance().getStringProperty(RATConstants.CommandsTemplateUUID);
		if(!Utils.isUUID(commandsTemplateUUID.toString())){
			throw new Exception();
			// TODO: log
		}
		this.loadCommandTemplates(templatesPath, json, commandsTemplateUUID);
		
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
		this.loadCommandTemplates(templatesPath, json, queriesTemplateUUID);
		
		// COMMENT: le istruzioni qui commentate, per semplificare l'avvio del sistema in questa fase, sono state spostate in InitDB
//		// TODO: per ora eseguo tutto in modo sincrono, in seconda battuta vedremo. Questa parte è tutta da rivedere in quanto
//		// i nomi dei parametri sono contenuti nelle due funzioni che seguono e così non va bene.
//		String rootDomainUUID = SystemInitializerHelpers.createRootDomain("AddRootDomain.conf", commandsTemplateUUID, queriesTemplateUUID);
//		
//		String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
//		String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
//		SystemInitializerHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", rootDomainUUID, userAdminName, userAdminPwd);
		
		// COMMENT: restituisco la connection al pool delle connection
//		_storage.shutDown();
	}
	
	private void loadCommandTemplates(String commandTemplatesPath, String json, String commandUUID) throws Exception{
		// COMMENT: Leggo il file contenente i comandi
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
		}
		catch(Exception e){
			e.printStackTrace();
			_storage.rollBack();
		}
	}
}
