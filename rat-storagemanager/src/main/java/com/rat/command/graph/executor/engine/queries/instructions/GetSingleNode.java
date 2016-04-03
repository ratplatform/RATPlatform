package com.rat.command.graph.executor.engine.queries.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.queries.QueryHelpers;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class GetSingleNode implements IInstruction{

	public GetSingleNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String paramName = invoker.getNodeParamValue("paramName");
		if(paramName == null){
			throw new Exception();
			// TODO: log
		}
		Object paramValue = invoker.getNodeParamValue("paramValue");
		if(paramValue == null){
			throw new Exception();
			// TODO: log
		}
		
		List<Vertex>list = invoker.getStorage().getVertices("rootvertices", paramName, paramValue);
		
		// TODO: orribile, ma per ora me la risolvo così: gli indici Tinkerpop non supportano query
		Vertex rootVertex = null;
		List<Vertex> results = new ArrayList<Vertex>();
		if(list.isEmpty()){
			Graph graph = new TinkerGraph();
			rootVertex = graph.addVertex(null);
			rootVertex.setProperty(RATConstants.VertexTypeField, VertexType.Empty);
			rootVertex.setProperty(RATConstants.VertexContentField, VertexType.Empty.toString());
			rootVertex.setProperty(RATConstants.VertexLabelField, VertexType.Empty.toString());
			rootVertex.setProperty(RATConstants.VertexUUIDField, UUID.randomUUID().toString());
		}
		else{
			rootVertex = list.get(0);
		}
		
		String strUUID = rootVertex.getProperty(RATConstants.VertexUUIDField);
		UUID rootUUID = UUID.fromString(strUUID);
		
		
		Graph graph = QueryHelpers.getResultGraph(rootVertex, results);
		
		QueryResult resultGraph = new QueryResult(rootUUID);
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		return resultGraph;
	}

}
