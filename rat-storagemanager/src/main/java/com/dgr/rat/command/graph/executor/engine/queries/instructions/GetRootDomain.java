package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.queries.QueryHelpers;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class GetRootDomain implements IInstruction{

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		// TODO: il parametro nodeType è inutile: da rivedere il meccanismo dei comandi
		String type = invoker.getNodeParamValue("nodeType");
		if(type == null){
			throw new Exception();
			// TODO log
		}
		
		UUID rootUUID = invoker.getStorage().getRootDomainUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		
		List<Vertex> results = new ArrayList<Vertex>();
		//results.add(rootVertex);
		Graph graph = QueryHelpers.getResultGraph(rootVertex, results);
		
		QueryResult resultGraph = new QueryResult(rootUUID);
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		return resultGraph;
	}

}
