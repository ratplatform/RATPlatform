/**
 * @author Daniele Grignani (dgr)
 * @date Oct 27, 2015
 */

package com.rat.command.graph.executor.engine.queries.instructions;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class StartStep implements IInstruction{

	public StartStep() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		if(invoker.getNumOfParameters() != 1){
			// TODO log
			throw new Exception();
		}
		
		//String rootNodeUUID = invoker.getNodeParamValue("rootNodeUUID");
		String rootNodeUUID = invoker.getParamValueByIndex(0);
		//String paramValue = invoker.getNodeParamValue(paramName);
		
		UUID rootUUID = UUID.fromString(rootNodeUUID);
		
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		//System.out.println(rootVertex);
		UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
		PipeResult queryResult = new PipeResult(nodeCallerInMemoryUUID);
		queryResult.setContent(queryPipe);
//		System.out.println("queryPipe: " + queryPipe.toString());
		
		// COMMENT: setto la rootUUID che poi mi servirà nell'ultimo nodo eseguito: ExecuteQueryPipe
		queryResult.setRootUUID(rootUUID);
		
//		System.out.println("nodeCallerInMemoryUUID: " + nodeCallerInMemoryUUID);
//		System.out.println("rootDomainUUID: " + rootDomainUUID);
		
		return queryResult;
	}

}
