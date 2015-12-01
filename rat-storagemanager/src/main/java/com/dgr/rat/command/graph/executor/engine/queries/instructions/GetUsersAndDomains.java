package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.queries.QueryHelpers;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class GetUsersAndDomains implements IInstruction{

	public GetUsersAndDomains() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String edgeLabel = invoker.getNodeParamValue("edgeLabel");
		if(edgeLabel == null){
			throw new Exception();
			// TODO log
		}
		
		// COMMENT in questo caso devo avere un solo parametro disponibile
		Iterator<String>it = invoker.getParameterNameIterator();
		String paramName = it.next();
		if(paramName == null || paramName.length() < 1){
			throw new Exception();
			// TODO log
		}
		String paramValue = invoker.getNodeParamValue(paramName);
		if(paramValue == null || paramValue.length() < 1){
			throw new Exception();
			// TODO log
		}
		
		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
		InstructionResultContainer commandResponse = invoker.getInstructionResult(nodeUUID);
		if(commandResponse == null){
			throw new Exception();
			// TODO: log
		}
		// COMMENT: in questo caso posso dare per scontato che commandResponse contenga un solo valore
		IInstructionResult instructionResult = commandResponse.pollByInMemoryUUID(nodeUUID);
		if(instructionResult == null){
			throw new Exception();
			// TODO: log
		}
		
		PipeResult queryResult = instructionResult.getContent(PipeResult.class);
		if(queryResult == null){
			throw new Exception();
			// TODO: log
		}

		GremlinPipeline<Vertex, Vertex> pipe = queryResult.getContent();
		@SuppressWarnings("unchecked")
		List<Vertex> results = (List<Vertex>) pipe.both(edgeLabel).has(paramName, paramValue).toList();
		
		UUID rootUUID = queryResult.getRootUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		
		Graph graph = QueryHelpers.getResultGraph(rootVertex, results);
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		
		return resultGraph;
	}

}
