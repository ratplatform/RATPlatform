/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.Iterator;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResult;
import com.dgr.rat.storage.provider.IStorage;
import com.tinkerpop.blueprints.Vertex;

public class SetVertexProperty implements IInstruction{

	public SetVertexProperty() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.AbstractInstruction#execute(com.dgr.rat.command.graph.instruction.IInvoker, com.dgr.rat.command.graph.ICommandProxyNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		UUID vertexCallerUUID = nodeCaller.getStoredNodeUUID();
		
		Iterator<String>it = invoker.getParameterNameIterator();
		while(it.hasNext()){
			String paramName = it.next();
			String paramValue = invoker.getNodeParamValue(paramName);
			
			Vertex vertexCaller = storage.getVertex(vertexCallerUUID);
			vertexCaller.setProperty(paramName, paramValue);
		}
		
//		storage.commit();

		InstructionResult commandResult = new InstructionResult(nodeCaller.getInMemoryNodeUUID());
		commandResult.setNewObjectUUID(vertexCallerUUID);
		
		// COMMENT: i risultati dell'instruction SetVertexProperty non mi interessano...
		return commandResult;
	}
}
