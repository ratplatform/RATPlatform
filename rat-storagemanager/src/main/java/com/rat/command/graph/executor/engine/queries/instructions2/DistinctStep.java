/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.rat.command.graph.executor.engine.queries.instructions2;

import java.util.ArrayList;
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
import com.tinkerpop.pipes.util.PipesFunction;

public class DistinctStep implements IInstruction{
	static private List<Vertex>list = new ArrayList<Vertex>();
	
	public DistinctStep() {

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
			list.clear();
			pipe.filter(aggregateFunction);
			//List<Vertex> results = (List<Vertex>) pipe.toList();
			//System.out.println(results.size());
			
			System.out.println("DistinctStep: " + pipe.toString());
			//System.out.println("DistinctStep size: " + pipe.size());
	
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
	
	private static final PipesFunction<Vertex, Boolean> aggregateFunction = new PipesFunction<Vertex, Boolean>(){
		
		@Override
		public Boolean compute(Vertex argument) {
			if(list.contains(argument)){
				return false;
			}
			else{
				list.add(argument);
				
				return true;
			}
		}
	};
}
