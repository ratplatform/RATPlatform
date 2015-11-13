/**
 * @author Daniele Grignani (dgr)
 * @date Sep 15, 2015
 */

package com.dgr.rat.graphgenerator.queries;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.graphgenerator.MakeSigmaJSON;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.PipesFunction;

public class QueryGraphGeneratorTest {
	public static final String CommandUUID = "42fc1097-b340-41a1-8ab2-e4d718ad48b9";
	
	List<String>_ratJSONs = new ArrayList<String>();
	Map<String, String>_clientJsonCommands = new HashMap<String, String>();
	
	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			return true;
		}
	};
	
	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> emitFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			Vertex vertex = argument.getObject();
			if(vertex == null){
//				System.out.println("vertex == null");
				return false;
			}
			Object property = vertex.getProperty(RATConstants.VertexTypeField);
			if(property == null){
//				System.out.println("property == null");
				return false;
			}
			
        	boolean result = property.toString().equalsIgnoreCase(VertexType.QueryPivot.toString());
            return result;
		}
	};
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void addNodesToGraph(String fileName, String commandVersion) throws Exception {
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		String ratJson = FileUtils.fileRead(GraphGeneratorHelpers.CommandTemplatesFolder + FileSystems.getDefault().getSeparator() + fileName);
		
		Graph commandGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJson);
		String rootVertexUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.RootVertexUUID);
		if(!Utils.isUUID(rootVertexUUID)){
			// TODO exception
		}
		Iterable<Vertex>iterable = commandGraph.getVertices(RATConstants.VertexUUIDField, rootVertexUUID);
		Vertex commandVertex = iterable.iterator().next();
		if(commandVertex == null){
			// TODO exception
		}
		
		// TODO: se il nodo ha un figlio che punta al nodo stesso, entra in un loop infinito: non fa alcun controllo sui nodi
		// attraversati; meglio usare una versione di traverse: meno elegante ma più sicura
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandVertex);
		List<List> lists = p.out().loop(1, QueryGraphGeneratorTest.whileFunction, QueryGraphGeneratorTest.emitFunction).path().toList();
		
		for(List<Vertex> list : lists){
			
			QueryFrame query = new QueryFrame(commandVersion);
//			query.set_instructionName(instructionName);
//			query.set_centralPipeName(centralPipeName);
			query.addNodesToGraph(list, commandVertex);
			
			this.writeAll(query, placeHolder, applicationName, applicationVersion);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void addBindGraphNodesToGraph(String fileName, String commandVersion) throws Exception {
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		String ratJson = FileUtils.fileRead(GraphGeneratorHelpers.CommandTemplatesFolder + FileSystems.getDefault().getSeparator() + fileName);
		
		Graph commandGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJson);
		String rootVertexUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.RootVertexUUID);
		if(!Utils.isUUID(rootVertexUUID)){
			// TODO exception
		}
		Iterable<Vertex>iterable = commandGraph.getVertices(RATConstants.VertexUUIDField, rootVertexUUID);
		Vertex commandVertex = iterable.iterator().next();
		if(commandVertex == null){
			// TODO exception
		}
		
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandVertex);
		List<Vertex> vertices = p.out().loop(1, QueryGraphGeneratorTest.whileFunction, QueryGraphGeneratorTest.emitFunction).filter(QueryGraphGeneratorTest.filterFunction).toList();
		System.out.println(vertices.toString());
		for(Vertex vertex : vertices){
			this.traverse(vertex);
		}
	}
	
	private static final PipeFunction<Edge, Boolean> edgeFilterFunction = new PipesFunction<Edge, Boolean>(){
		@Override
		public Boolean compute(Edge edge){
			boolean result = false;
			Vertex vertex = edge.getVertex(Direction.OUT);
			String content = vertex.getProperty(RATConstants.VertexTypeField);
			
			result = content.equalsIgnoreCase(VertexType.SystemKey.toString()) ? true : false;
			return result;
		}
	};
	
	// COMMENT: NON CANCELLARE!
	private void traverse(Vertex queryPivotVertex){
		Stack<Vertex>stack = new Stack<Vertex>();
		List<Vertex> visited = new LinkedList<Vertex>();
		stack.push(queryPivotVertex);
		// COMMENT: do per scontato che la label dell'edge che collega la queryPivotVertex alla sua systemKey abbia
		// lo stesso nome delle edge che collegano le systemkey tra loro. Inoltre do per scontato che 
		// queryPivotVertex abbia sempre una sola edge inE
		GremlinPipeline<Vertex, Edge> p = new GremlinPipeline<Vertex, Edge>(queryPivotVertex);
		Edge edge = p.inE().filter(QueryGraphGeneratorTest.edgeFilterFunction).next();
		String edgeLabel = edge.getLabel();

		while(!stack.isEmpty()){
			Vertex parent = stack.pop();
			visited.add(parent);
			System.out.println(parent);
			String vertexType = parent.getProperty(RATConstants.VertexTypeField).toString();
			Iterator<Vertex>it = parent.getVertices(Direction.BOTH, edgeLabel).iterator();
			while(it.hasNext()){
				Vertex child = it.next();
				vertexType = child.getProperty(RATConstants.VertexTypeField).toString();
				if(VertexType.SystemKey.toString().equalsIgnoreCase(vertexType)){
					if(!visited.contains(child) ){
						Direction direction = this.getDirection(Direction.OUT, parent, child, edgeLabel);
						if(direction == null){
							direction = this.getDirection(Direction.IN, parent, child, edgeLabel);
						}
//						System.out.println("Da " + parent + " al child " +  child + " la direzione è: " + direction);
						
						stack.push(child);
					}
				}
			}
		}
	}
	
	private Direction getDirection(Direction startDirection, Vertex parent, Vertex child, String label){
		Iterator<Edge>it = parent.getEdges(startDirection, label).iterator();
		Direction result = null;
		
		while(it.hasNext()){
			Edge edge = it.next();
			Vertex vertex = edge.getVertex(Direction.OUT);
			if(vertex == child){
				result = Direction.OUT;
				break;
			}
			else{
				vertex = edge.getVertex(Direction.IN);
				if(vertex == child){
					result = Direction.IN;
					break;
				}
			}
		}
		return result;
	}
	
	private static final PipeFunction<Vertex, Boolean> filterFunction = new PipesFunction<Vertex, Boolean>(){
		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String content = vertex.getProperty(RATConstants.VertexTypeField);
			
			result = content.equalsIgnoreCase(VertexType.QueryPivot.toString()) ? true : false;
			return result;
		}
	};
	
	private void addQueries() throws Exception{
		RATHelpers.initProperties(GraphGeneratorHelpers.StorageManagerPropertyFile);
		
		// TODO: prendere tutti i file direttamente dentro la directory
		this.addNodesToGraph("AddNewDomainTemplate.conf", "0.1");
		this.addNodesToGraph("AddNewUserTemplate.conf", "0.1");
		this.addNodesToGraph("AddCommentTemplate.conf", "0.1");
		this.addNodesToGraph("BindFromDomainToUserTemplate.conf", "0.1");
		this.addNodesToGraph("BindFromUserToDomainTemplate.conf", "0.1");
		this.addNodesToGraph("AddSubCommentTemplate.conf", "0.1");
		this.addNodesToGraph("AddCommentTemplate.conf", "0.1");
//		this.addNodesToGraph("AddRootDomainTemplate.conf", "0.1");
		
//		this.addBindGraphNodesToGraph("BindGraphTemplate.conf", "0.1");
		
	}
	
	@Test
	public void test() {
		try {
			this.addQueries();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeAll(QueryFrame query, String placeHolder, String applicationName, String applicationVersion) throws Exception{
		JsonHeader header = new JsonHeader();
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setCommandVersion(query.get_commandVersion());
		header.setDomainName(placeHolder);
		header.setMessageType(MessageType.Template);
		header.setCommandType(query.get_commandType());
		header.setCommandName(query.getEndPipeInstruction());
		header.setCommandGraphUUID(query.get_commandUUID());
		header.setRootVertexUUID(query.get_rootNodeUUID());
		
		String path = GraphGeneratorHelpers.QueriesFolder + GraphGeneratorHelpers.PathSeparator + query.getEndPipeInstruction() + ".conf";
		String remoteRequestJson = JSONObjectBuilder.buildRemoteQuery(header, query.get_rootNode());
		
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson));
		GraphGeneratorHelpers.writeGraphToJson(remoteRequestJson, path);
		
		String commandTemplate = JSONObjectBuilder.buildCommandTemplate(header, query.getGraph());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		path = GraphGeneratorHelpers.QueryTemplatesFolder + GraphGeneratorHelpers.PathSeparator + query.getEndPipeInstruction() + "Template.conf";
		GraphGeneratorHelpers.writeGraphToJson(commandTemplate, path);

		//Alchemy command template JSON
		String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(commandTemplate);
		GraphGeneratorHelpers.writeAlchemyJson(query.getEndPipeInstruction() + "Template", query.get_commandVersion(), alchemyJSON, "queries");

		this.saveForTest(commandTemplate, remoteRequestJson);
//		JSONObjectBuilder.buildQuery(header, command.get_rootNode().getNode().asVertex());
	}
	
	private void saveForTest(String ratJson, String clientJsonCommand) throws JsonParseException, JsonMappingException, IOException{
		_ratJSONs.add(ratJson);
		
		String commandName = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.CommandName);
		_clientJsonCommands.put(commandName, clientJsonCommand);
	}
	
	@After
	public void after(){
		try {
//			this.verifyUUID();
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
}
