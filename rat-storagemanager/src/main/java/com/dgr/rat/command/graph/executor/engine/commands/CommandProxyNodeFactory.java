/**
 * @author Daniele Grignani (dgr)
 * @date Sep 13, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.CommandData;
import com.dgr.rat.command.graph.executor.engine.ICommandGraphData;
import com.dgr.rat.command.graph.executor.engine.ICommandGraphVisitableFactory;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.storage.provider.IStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;

public class CommandProxyNodeFactory implements ICommandGraphVisitableFactory{
	
	public CommandProxyNodeFactory() {

	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandGraphVisitableFactory#buildGraph(com.dgr.rat.command.graph.CommandData)
	 */
	@Override
	public ICommandGraphData buildGraph(CommandData commandData) throws Exception{
		UUID commandUUID = commandData.getCommandUUID();
		CommandTemplateGraph graph = null;
		IStorage storage = commandData.getStorage();
		try{
		if(commandUUID != null){
			// Prendo il nodo root del comando
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
			
			// TODO: eliminare CommandTemplateGraph e portare le sue funzioni qui
			graph = new CommandTemplateGraph();
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

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandGraphVisitableFactory#buildNode(com.dgr.rat.command.graph.CommandData, com.dgr.rat.graphgenerator.instruction.wrappers.RATNode)
	 */
	@Override
	public ICommandNodeVisitable buildNode(CommandData commandData) throws Exception {
		ICommandGraphData graph = this.buildGraph(commandData);
		ICommandNodeVisitable visitable = graph.getRootNode(this);
		
		return visitable;
	}
}
