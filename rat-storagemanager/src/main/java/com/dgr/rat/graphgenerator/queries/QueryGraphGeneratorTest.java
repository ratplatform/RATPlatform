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
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeAlchemyJSON;
import com.dgr.rat.json.utils.RATJsonUtils;

import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;


public class QueryGraphGeneratorTest {
	private BuildQueryJavaScript _buildQueryJavaScript = new BuildQueryJavaScript();
	
	private void addQueries() throws Exception{
		RATUtils.initProperties(GraphGeneratorHelpers.StorageManagerPropertyFile);
		String queryVersion = "0.1";
		
		this.addQuery("AddRootDomainAdminUserTemplate.conf", queryVersion);
		this.addQuery("AddNewUserTemplate.conf", queryVersion);
		this.addQuery("AddNewDomainTemplate.conf", queryVersion);
		this.addQuery("BindFromUserToDomainTemplate.conf", queryVersion);
		this.addQuery("AddCommentTemplate.conf", queryVersion);
		this.addQuery("AddRootDomainTemplate.conf", queryVersion);
		
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
		String alchemyJSON = MakeAlchemyJSON.fromRatJsonToAlchemy(commandTemplate);
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
