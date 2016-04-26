/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.rat.command.graph.executor.engine.queries.instructions2;

import java.util.List;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class EdgeInStep implements IInstruction{

	public EdgeInStep() {

	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		try{
//			String edgeLabel = nodeCaller.getProperty("edgeLabel").toString();
			String edgeLabel = invoker.getNodeParamValue("edgeLabel");
	
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
			pipe.in(edgeLabel);
			
			Step step = new Step();
			step.paramValue = edgeLabel;
			step.stepType = Counter.StepType.In;
			Counter.getInstance().addStep(step);
			System.out.println("EdgeInStep: " + pipe.toString());
			Counter.getInstance().run();
			/*
			if(Counter.getInstance().count == 1){ 
				List<Vertex> results = (List<Vertex>) pipe.toList();
				System.out.println(results.size());
			}
			Counter.getInstance().inc();
			*/
			
			//System.out.println("EdgeInStep size: " + pipe.size());
			
			UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
			PipeResult newQueryResult = new PipeResult(nodeCallerInMemoryUUID);
			newQueryResult.setContent(pipe);
			newQueryResult.setRootUUID(queryResult.getRootUUID());
			
	//		System.out.println("nodeUUID: " + nodeUUID);
	//		System.out.println("nodeCallerInMemoryUUID: " + nodeCallerInMemoryUUID);
	//		System.out.println("queryResult.getRootUUID(): " + queryResult.getRootUUID());
			
			return newQueryResult;
		}
		catch(Exception e){
			throw new Exception(e);
		}
	}
}
