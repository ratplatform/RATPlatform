package com.rat.command.graph.executor.engine.queries.instructions;

import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class CountStep implements IInstruction{

	public CountStep() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		try{
			// COMMENT il nodeCaller non è il nodo che ha generato il valore che mi interessa, ma è il parent di caller
			// ad averlo fatto... Infatti nodeCaller è quello corrente, ossia il nodo al quale è collecata questa instruction.
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
			long result = pipe.count();
					
			UUID rootUUID = queryResult.getRootUUID();
			
			Graph graph = new TinkerGraph();
			Vertex newVertex = graph.addVertex(null);
			newVertex.setProperty(RATConstants.VertexIsRootField, true);
			newVertex.setProperty(RATConstants.SubNodes, result);
			
			QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
			resultGraph.setRootUUID(rootUUID);
			resultGraph.setGraph(graph);
			
			invoker.addCommandResponse(nodeCaller, resultGraph);
			
			return resultGraph;
		}
		catch(Exception e){
			throw new Exception(e);
		}
	}

}
