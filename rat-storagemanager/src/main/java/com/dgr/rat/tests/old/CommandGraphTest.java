/**
 * @author Daniele Grignani (dgr)
 * @date Aug 23, 2015
 */

package com.dgr.rat.tests.old;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

import com.dgr.rat.command.graph.executor.engine.commands.CommandTemplateGraph;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.main.SystemCommandsInitializer;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.TinkerGraphStorage;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import java.util.UUID;

public class CommandGraphTest {
	private RATJsonObject _jsonHeader = null;
	private static final String[] Commands = new String[]{"CreateRootDomain", "CreateNewRootUser" };
	private List<CommandTemplateGraph> _list = new LinkedList<CommandTemplateGraph>();
	
	@Test
	public void test() {
		try {
			IStorage storage = StorageBridge.getInstance().getStorage();
			
			for(CommandTemplateGraph commandGraph : _list){
//				DataWrapper dataWrapper = new DataWrapper(_jsonHeader, commandGraph);
//				CommandGraphVisitor visitor = new CommandGraphVisitor(storage);
//				dataWrapper.visit(visitor, SchemaVertexWrapper.class);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeGraphToJson(String json, String path) throws IOException{
        BufferedWriter writer = null;
        
        try {
            File file = new File(path);

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(RATJsonUtils.jsonPrettyPrinter(json));
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            try {
                writer.close();
            } 
            catch (Exception e) {
            }
        }
    }
	
	@SuppressWarnings("unused")
	private void getJsonGraphFromStorage() throws Exception{
		IStorage storage = StorageBridge.getInstance().getStorage();
		Graph graph = storage.getGraph();
		
		GremlinPipeline<Vertex, String> pipe = new GremlinPipeline<Vertex, String>(graph);
		Map map = (Map) pipe.outV();
	}
	
	@After
	public void verify(){
		try {
//			this.getJsonGraphFromStorage();
			
			this.verifyNumOfEdges();
			this.verifyNumOfVertices();
			this.verifyGraphs();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Anche in caso di exception voglio che scriva lo stesso i risultati per vederli
		try {
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String appPath = AppProperties.getInstance().getStringProperty("sigma.path");
			String dataFolder = AppProperties.getInstance().getStringProperty("sigma.data.folder");
			String resultFilename = this.getClass().getSimpleName() + "Result";
			String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
			
			String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
			String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");

			String text = FileUtils.fileRead(appPath + FileSystems.getDefault().getSeparator() + pageTemplate);
			
			String path = appPath + FileSystems.getDefault().getSeparator() + dataFolder + FileSystems.getDefault().getSeparator() + resultFilename + ".json";
			String html = text.replace(resultPlaceholder, path);
			html = html.replace(pageTitlePlaceholder, resultFilename);
			FileUtils.write(appPath + FileSystems.getDefault().getSeparator() + resultFilename + ".html", html, false);
			
			this.dumpJson(path);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verifyGraphs() throws Exception{
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		
		List<Vertex> rootDomains = new ArrayList<Vertex>();
		List<String> rootDomainsChildren = new ArrayList<String>();
		List<String> rootDomainsInDB = new ArrayList<String>();
		List<String> rootDomainsInDBChildren = new ArrayList<String>();
		
		for(CommandTemplateGraph commandGraph : _list){
			Iterator<Vertex> it = commandGraph.getVertices().iterator();
			while (it.hasNext()){
				Vertex vertex = it.next();
				String strUUID = vertex.getProperty(RATConstants.VertexUUIDField);
				Assert.assertNotNull(strUUID);
				UUID uuid = UUID.fromString(strUUID);
				Vertex vertexFromDB = storage.getVertex(uuid);
				Assert.assertNotNull(vertexFromDB);
				
				String propertyValue = vertex.getProperty(RATConstants.VertexTypeField);
				Set<String> neighborUUIDsFromDB = new HashSet<String>();
				if(propertyValue == null || !propertyValue.equals(RATConstants.VertexTypeValueRootDomain)){
					Iterable<Vertex> iterable = vertexFromDB.getVertices(Direction.BOTH);
					Iterator<Vertex> vertexFromDBIt = iterable.iterator();
					while(vertexFromDBIt.hasNext()){
						Vertex neighbor = vertexFromDBIt.next();
						Assert.assertTrue(neighborUUIDsFromDB.add(neighbor.getProperty(RATConstants.VertexUUIDField).toString()));
					}
				}
				else{
					rootDomains.add(vertex);
//					System.out.println("propertyValue: " + propertyValue);
				}
				
				Set<String> neighbours = new HashSet<String>();
				propertyValue = vertexFromDB.getProperty(RATConstants.VertexTypeField);
				if(propertyValue == null || !propertyValue.equals(RATConstants.VertexTypeValueRootDomain)){
					Iterable<Vertex> iterable = vertex.getVertices(Direction.BOTH);
					Iterator<Vertex> vertexIt = iterable.iterator();
					while(vertexIt.hasNext()){
						Vertex neighbor = vertexIt.next();
						Assert.assertTrue(neighbours.add(neighbor.getProperty(RATConstants.VertexUUIDField).toString()));
					}
				}
				else{
					if(!rootDomainsInDB.contains(vertexFromDB.getProperty(RATConstants.VertexUUIDField).toString())){
						rootDomainsInDB.add(vertexFromDB.getProperty(RATConstants.VertexUUIDField).toString());
					}
//					System.out.println("propertyValue: " + propertyValue);
				}
				
				if(neighborUUIDsFromDB.size() != neighbours.size()){
					System.out.println("neighborUUIDsFromDB UUID: " + neighborUUIDsFromDB.size());
					System.out.println("neighbours UUID: " + neighbours.size());
					
					System.out.println("VertexFromDB VertexValue: " + vertexFromDB.getProperty(RATConstants.VertexLabelField));
					System.out.println("VertexFromDB VertexType: " + vertexFromDB.getProperty(RATConstants.VertexTypeField));
					System.out.println("Vertex VertexValue: " + vertex.getProperty(RATConstants.VertexLabelField));
					System.out.println("Vertex VertexType: " + vertex.getProperty(RATConstants.VertexTypeField));
				}
				Assert.assertEquals(neighborUUIDsFromDB.size(), neighbours.size());
				
				Iterator<String> uuidIt = neighbours.iterator();
				while(uuidIt.hasNext()){
					strUUID = uuidIt.next();
					Assert.assertTrue(neighborUUIDsFromDB.contains(strUUID));
				}
			}
		}
		
		System.out.println("rootDomainsInDB size: " + rootDomainsInDB.size());
		System.out.println("rootDomains size: " + rootDomains.size());
		// Nel DB ci deve essere un solo RootDomain
		Assert.assertEquals(rootDomainsInDB.size(), 1);
		// Invece nei grafi generati a partire dai due json i RootDomain devono essere 2 (uno per json)
		Assert.assertEquals(rootDomains.size(), 2);
				
		Vertex vertexRootInDB = storage.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		Assert.assertNotNull(vertexRootInDB);
		Iterator<Vertex> dbIt = vertexRootInDB.getVertices(Direction.BOTH).iterator();
		while (dbIt.hasNext()){
			Vertex vertex = dbIt.next();
			String strUUID = vertex.getProperty(RATConstants.VertexUUIDField);
			Assert.assertNotNull(strUUID);
			rootDomainsInDBChildren.add(strUUID);
		}
		
		Iterator<Vertex> it = rootDomains.iterator();
		while (it.hasNext()){
			Vertex vertex = it.next();
			Iterator<Vertex> childIt = vertex.getVertices(Direction.BOTH).iterator();
			while (childIt.hasNext()){
				Vertex child = childIt.next();
				String strUUID = child.getProperty(RATConstants.VertexUUIDField);
				Assert.assertNotNull(strUUID);
				rootDomainsChildren.add(strUUID);
			}
		}
		
		// Il numero di children deve essere uguale per entrambi
		System.out.println("rootDomainsInDBChildren size: " + rootDomainsInDBChildren.size());
		System.out.println("rootDomainsChildren size: " + rootDomainsChildren.size());
		Assert.assertEquals(rootDomainsInDBChildren.size(), rootDomainsChildren.size());

		// Devono contenere gli stessi children
		Iterator<String> childIt = rootDomainsChildren.iterator();
		while (childIt.hasNext()){
			String strUUID = childIt.next();
			Assert.assertTrue(rootDomainsInDBChildren.contains(strUUID));
		}
	}
	
	public void verifyNumOfVertices() throws Exception{
		// Lo storage potrebbe avere meno vertici rispetto 
		// alla somma dei grafi dei comandi in quanto questi ultimi potrebbero avere alcuni vertici
		// in comune (che vengono bindat nel db)
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		Iterator<Vertex> it = graph.getVertices().iterator();
		HashMap<UUID, Vertex>verticesFromDB = new HashMap<UUID, Vertex>();
		while (it.hasNext()){
			Vertex vertex = it.next();
			verticesFromDB.put(UUID.fromString(vertex.getProperty(RATConstants.VertexUUIDField).toString()), vertex);
		}
		
		int inc = 0;
		HashMap<UUID, Vertex>vertices = new HashMap<UUID, Vertex>();
		for(CommandTemplateGraph commandGraph : _list){
			it = commandGraph.getVertices().iterator();
			while (it.hasNext()){
				Vertex vertex = it.next();
				
				UUID uuid = UUID.fromString(vertex.getProperty(RATConstants.VertexUUIDField).toString());
				if(!vertices.containsKey(uuid)){
					vertices.put(UUID.fromString(vertex.getProperty(RATConstants.VertexUUIDField).toString()), vertex);
				}
				else{
					System.out.println("verticesFromDB not contains " + uuid.toString());
					inc++;
				}
			}
		}
		
		System.out.println("vertices: " + vertices.size());
		System.out.println("verticesFromDB: " + verticesFromDB.size());
		System.out.println("duplicated elements " + inc);
		
		Assert.assertEquals(vertices.size(), verticesFromDB.size());
		Assert.assertEquals(inc, 1);
	}
	
	public void verifyNumOfEdges() throws Exception{
		ArrayList<Edge>edges = new ArrayList<Edge>();
		for(CommandTemplateGraph commandGraph : _list){
			Iterator<Edge> it = commandGraph.getEdges().iterator();
			while (it.hasNext()){
				edges.add(it.next());
			}
		}
		
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		Iterator<Edge> it = graph.getEdges().iterator();
		ArrayList<Edge>edgesFromDB = new ArrayList<Edge>();
		while (it.hasNext()){
			edgesFromDB.add(it.next());
		}
		System.out.println("edges: " + edges.size());
		System.out.println("edgesFromDB: " + edgesFromDB.size());
		Assert.assertEquals(edges.size(), edgesFromDB.size());
		
		if(edges.size() != edgesFromDB.size()){
			throw new Exception();
		}
	}

	public void dumpJson(String path) throws Exception{
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		
		String result = MakeSigmaJSON.fromRatJsonToAlchemy(graph);
//		System.out.println(result);
		
		try {
			this.writeGraphToJson(result, path);
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Before
	public void init(){
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			
			String path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "start-systemcommands.xml";
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);
			SystemCommandsInitializer systemCommandsInitializer = (SystemCommandsInitializer)context.getBean("InitSystemCommands");
			// 1) Inizializzo il database (se non esiste il DB allora lo creo)
		
			systemCommandsInitializer.initStorage();
			
			for(String command : Commands){
				this.createGraph(command);
			}
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createGraph(String command){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			String json = this.getJSON(command);
			
			_jsonHeader = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
			
			mapper = new ObjectMapper();
			String output = mapper.writeValueAsString(_jsonHeader.getSettings());
			JsonNode actualObj = mapper.readTree(output);
			CommandTemplateGraph commandGraph = new CommandTemplateGraph();
			InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
			GraphSONReader.inputGraph(commandGraph, inputStream);
			_list.add(commandGraph);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getJSON(String command) throws Exception{
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(RATConstants.ConfigurationFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		String systemCommandsFolder = AppProperties.getInstance().getStringProperty(RATConstants.CommandTemplatesFolder);
		pathBuffer.append(systemCommandsFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		pathBuffer.append(command);
		pathBuffer.append(".conf");
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String rootDomain = AppProperties.getInstance().getStringProperty(RATConstants.RootDomain);
		input = input.replace(placeHolder, rootDomain);
		
		return input;
	}
}
