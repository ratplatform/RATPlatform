package com.rat.command.graph.executor.engine.queries.instructions;

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

public class EndStep implements IInstruction{

	public EndStep() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
		InstructionResultContainer commandResponse = invoker.getInstructionResult(nodeUUID);
		if(commandResponse == null){
			throw new Exception();
			// TODO: log
		}

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
		//System.out.println("EndStep: " + pipe.toString());
		List<Vertex> results = (List<Vertex>) pipe.toList();
		
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
