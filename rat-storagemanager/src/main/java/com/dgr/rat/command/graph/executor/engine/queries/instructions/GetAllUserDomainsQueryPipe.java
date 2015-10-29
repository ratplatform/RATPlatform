/**
 * @author Daniele Grignani (dgr)
 * @date Oct 27, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class GetAllUserDomainsQueryPipe implements IInstruction{

	public GetAllUserDomainsQueryPipe() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String content = invoker.getParamValue(RATConstants.VertexContentField);
		String edgeLabel = invoker.getParamValue("edgeLabel");
		
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
		pipe.out(edgeLabel).has(RATConstants.VertexContentField, content);
		
		UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
		PipeResult newQueryResult = new PipeResult(nodeCallerInMemoryUUID);
		newQueryResult.setContent(pipe);
		newQueryResult.setRootUUID(queryResult.getRootUUID());
		
		return newQueryResult;
	}

}
