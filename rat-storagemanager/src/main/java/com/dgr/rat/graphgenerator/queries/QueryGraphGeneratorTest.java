/**
 * @author Daniele Grignani (dgr)
 * @date Sep 15, 2015
 */

package com.dgr.rat.graphgenerator.queries;


import java.nio.file.FileSystems;
import java.util.List;
import org.junit.Test;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;

import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;


public class QueryGraphGeneratorTest {
	private BuildQueryJavaScript _buildQueryJavaScript = new BuildQueryJavaScript();
	
//	public static final String CommandUUID = "42fc1097-b340-41a1-8ab2-e4d718ad48b9";
//	
//	List<String>_ratJSONs = new ArrayList<String>();
//	Map<String, String>_clientJsonCommands = new HashMap<String, String>();
//	
////	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
////		@Override
////		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
////			return true;
////		}
////	};
////	
////	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> emitFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
////		@Override
////		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
////			Vertex vertex = argument.getObject();
////			if(vertex == null){
//////				System.out.println("vertex == null");
////				return false;
////			}
////			Object property = vertex.getProperty(RATConstants.VertexTypeField);
////			if(property == null){
//////				System.out.println("property == null");
////				return false;
////			}
////			
////        	boolean result = property.toString().equalsIgnoreCase(VertexType.QueryPivot.toString());
////            return result;
////		}
////	};
//	
//	private void addQueries() throws Exception{
//		RATHelpers.initProperties(GraphGeneratorHelpers.StorageManagerPropertyFile);
//		BuildQueryJavaScript buildQueryJavaScript = new BuildQueryJavaScript();
//		String queryVersion = "0.1";
//		// TODO: prendere tutti i file direttamente dentro la directory
//		this.addNodesToGraph("AddRootDomainAdminUserTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
//		this.addNodesToGraph("AddRootDomainAdminUserTemplate.conf", queryVersion, buildQueryJavaScript, "email", ReturnType.string, "GetAllAdminUsersByEmail");
//		
////		this.addNodesToGraph("AddNewDomainTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
////		this.addNodesToGraph("AddNewUserTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
////		this.addNodesToGraph("AddCommentTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
//		this.addNodesToGraph("BindFromDomainToUserTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
////		this.addNodesToGraph("AddSubCommentTemplate.conf", queryVersion, buildQueryJavaScript, null, null, null);
////		this.addNodesToGraph("BindFromUserToDomainTemplate.conf", queryVersion, buildQueryJavaScript, "domainName", ReturnType.string, "GetUserDomainByName");
//		
//		String javaScript = buildQueryJavaScript.getJavaScript();
////		System.out.println(javaScript);
//		GraphGeneratorHelpers.writeJavaScript("queries", javaScript);
//		
////		this.addNodesToGraph("AddRootDomainTemplate.conf", "0.1");
//		
////		this.addBindGraphNodesToGraph("BindGraphTemplate.conf", "0.1");
//	}
//	
//	@SuppressWarnings({ "rawtypes", "deprecation" })
//	public void addNodesToGraph(String fileName, String commandVersion, BuildQueryJavaScript buildQueryJavaScript, String startPipeParam, ReturnType type, String queryName) throws Exception {
//		String ratJson = FileUtils.fileRead(GraphGeneratorHelpers.CommandTemplatesFolder + FileSystems.getDefault().getSeparator() + fileName);
//		
//		Graph commandGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJson);
//		String rootVertexUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.RootVertexUUID);
//		if(!Utils.isUUID(rootVertexUUID)){
//			// TODO exception
//		}
//		Iterable<Vertex>iterable = commandGraph.getVertices(RATConstants.VertexUUIDField, rootVertexUUID);
//		Vertex commandVertex = iterable.iterator().next();
//		if(commandVertex == null){
//			// TODO exception
//		}
//		
//		// TODO: se il nodo ha un figlio che punta al nodo stesso, entra in un loop infinito: non fa alcun controllo sui nodi
//		// attraversati; meglio usare una versione di traverse: meno elegante ma più sicura
//		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandVertex);
//		List<List> lists = p.out().loop(1, QueryGraphGeneratorTest.whileFunction, QueryGraphGeneratorTest.emitFunction).path().toList();
//		
//		for(List<Vertex> list : lists){
//			
//			QueryFrame query = new QueryFrame(commandVersion);
//			query.addNodesToGraph(list, commandVertex, startPipeParam, type, queryName);
//			
//			this.writeAll(query, buildQueryJavaScript);
//		}
//	}
//	
//	public void writeAll(QueryFrame query, BuildQueryJavaScript buildQueryJavaScript) throws Exception{
//		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
//		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
//		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
//		
//		JsonHeader header = new JsonHeader();
//		header.setApplicationName(applicationName);
//		header.setApplicationVersion(applicationVersion);
//		header.setCommandVersion(query.get_commandVersion());
//		header.setDomainName(placeHolder);
//		header.setDomainUUID("null");
//		header.setMessageType(MessageType.Request);
//		header.setCommandType(query.get_commandType());
//		header.setCommandName(query.getEndPipeInstruction());
//		header.setCommandGraphUUID(query.get_commandUUID());
//		header.setRootVertexUUID(query.get_rootNodeUUID());
//		
//		String path = GraphGeneratorHelpers.QueriesFolder + GraphGeneratorHelpers.PathSeparator + query.getEndPipeInstruction() + ".conf";
//		String remoteRequestJson = JSONObjectBuilder.buildRemoteQuery(header, query.get_rootNode());
//		
//		RATJsonObject ratJsonObject = RATJsonUtils.getRATJsonObject(remoteRequestJson);
//		if(buildQueryJavaScript.getHeader() == null){
//			buildQueryJavaScript.setHeader(header);
//		}
//		buildQueryJavaScript.make(query.getEndPipeInstruction(), ratJsonObject);
//		
////		System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson));
//		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson), path);
//		
//		String commandTemplate = JSONObjectBuilder.buildCommandTemplate(header, query.getGraph());
////		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
//		path = GraphGeneratorHelpers.QueryTemplatesFolder + GraphGeneratorHelpers.PathSeparator + query.getEndPipeInstruction() + "Template.conf";
//		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(commandTemplate), path);
//
//		//Alchemy command template JSON
//		String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(commandTemplate);
//		GraphGeneratorHelpers.writeAlchemyJson(query.getEndPipeInstruction() + "Template", query.get_commandVersion(), alchemyJSON, "queries");
//
//		this.saveForTest(commandTemplate, remoteRequestJson);
////		JSONObjectBuilder.buildQuery(header, command.get_rootNode().getNode().asVertex());
//	}
//	
//	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
//		@Override
//		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
//			return true;
//		}
//	};
//	
//	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> emitFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
//		@Override
//		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
//			Vertex vertex = argument.getObject();
//			if(vertex == null){
////				System.out.println("vertex == null");
//				return false;
//			}
//			Object property = vertex.getProperty(RATConstants.VertexTypeField);
//			if(property == null){
////				System.out.println("property == null");
//				return false;
//			}
//			
//        	boolean result = property.toString().equalsIgnoreCase(VertexType.QueryPivot.toString());
//            return result;
//		}
//	};
	
	private void addQueries() throws Exception{
		RATHelpers.initProperties(GraphGeneratorHelpers.StorageManagerPropertyFile);
		String queryVersion = "0.1";
		
		this.addQuery("AddRootDomainAdminUserTemplate.conf", queryVersion);
		this.addQuery("AddNewUserTemplate.conf", queryVersion);
		this.addQuery("AddNewDomainTemplate.conf", queryVersion);
		this.addQuery("BindFromUserToDomainTemplate.conf", queryVersion);
		
		String javaScript = _buildQueryJavaScript.getJavaScript();
//		System.out.println(javaScript);
		GraphGeneratorHelpers.writeJavaScript("queries", javaScript);
	}
	
	public void addQuery(String fileName, String queryVersion) throws Exception {
		String ratJson = FileUtils.fileRead(GraphGeneratorHelpers.CommandTemplatesFolder + FileSystems.getDefault().getSeparator() + fileName);
		
		Graph commandGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJson);
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandGraph.getVertices());
		@SuppressWarnings("unchecked")
		List<Vertex> list = (List<Vertex>) p.outE(RATConstants.QueryPivotEdgeLabel).inV().has(RATConstants.IsRootQueryPivot, true).toList();
//		System.out.println(list.size());
		QueryGenerator queryGenerator = new QueryGenerator();
		for(Vertex vertex : list){
			queryGenerator.traverse(vertex);
			this.writeAll(queryGenerator, queryVersion);
		}
	}
	
	public void writeAll(QueryGenerator query, String queryVersion) throws Exception{
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		JsonHeader header = new JsonHeader();
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setCommandVersion(queryVersion);
		header.setDomainName(placeHolder);
		header.setDomainUUID("null");
		header.setMessageType(MessageType.Request);
		header.setCommandType(query.get_commandType());
		header.setCommandName(query.getCommandName());
		header.setCommandGraphUUID(query.get_commandUUID());
		header.setRootVertexUUID(query.get_rootNodeUUID());
		
		String path = GraphGeneratorHelpers.QueriesFolder + GraphGeneratorHelpers.PathSeparator + query.getCommandName() + ".conf";
		String remoteRequestJson = JSONObjectBuilder.buildRemoteQuery(header, query.get_rootNode());
		
		_buildQueryJavaScript.setHeader(header);
		RATJsonObject ratJsonObject = RATJsonUtils.getRATJsonObject(remoteRequestJson);
		_buildQueryJavaScript.make(query.getCommandName(), ratJsonObject);
		
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson));
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson), path);
		
		String commandTemplate = JSONObjectBuilder.buildCommandTemplate(header, query.getGraph());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		path = GraphGeneratorHelpers.QueryTemplatesFolder + GraphGeneratorHelpers.PathSeparator + query.getCommandName() + "Template.conf";
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(commandTemplate), path);

		//Alchemy command template JSON
		String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(commandTemplate);
		GraphGeneratorHelpers.writeAlchemyJson(query.getCommandName() + "Template", queryVersion, alchemyJSON, "queries");
	}
	
	@Test
	public void test() {
		try {
//			this.addQueries();
			this.addQueries();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private void saveForTest(String ratJson, String clientJsonCommand) throws JsonParseException, JsonMappingException, IOException{
//		_ratJSONs.add(ratJson);
//		
//		String commandName = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.CommandName);
//		_clientJsonCommands.put(commandName, clientJsonCommand);
//	}
}
