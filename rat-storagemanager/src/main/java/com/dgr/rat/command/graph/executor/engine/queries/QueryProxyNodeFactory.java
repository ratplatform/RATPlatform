/**
 * @author Daniele Grignani (dgr)
 * @date Oct 19, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.CommandData;
import com.dgr.rat.command.graph.executor.engine.ICommandGraphData;
import com.dgr.rat.command.graph.executor.engine.commands.CommandProxyNodeFactory;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.storage.provider.IStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;

public class QueryProxyNodeFactory extends CommandProxyNodeFactory{

	public QueryProxyNodeFactory() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ICommandGraphData buildGraph(CommandData commandData) throws Exception{
		UUID commandUUID = commandData.getCommandUUID();
		QueryTemplateGraph graph = null;
		IStorage storage = commandData.getStorage();
		try{
			if(commandUUID != null){
				// COMMENT Prendo il nodo root del comando
				storage.openConnection();
				Vertex vertex = storage.getVertex(commandUUID);
				if(vertex == null){
					// TODO: exception e log
				}
					
				String json = vertex.getProperty(RATConstants.VertexContentField);
	//				json = RATJsonUtils.jsonPrettyPrinter(json);
	//				System.out.println(json);
				ObjectMapper mapper = new ObjectMapper();
				RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
				String output = mapper.writeValueAsString(jsonHeader.getSettings());
				JsonNode actualObj = mapper.readTree(output);
				graph = new QueryTemplateGraph();
				InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
				GraphSONReader.inputGraph(graph, inputStream);
			}
			else{
				// TODO: exception e log
			}
		}
		catch(Exception e){
			throw new Exception(e);
		}
		
		finally{
			storage.shutDown();
		}
		
		return graph;
	}
}
