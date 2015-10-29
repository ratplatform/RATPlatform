/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;

/**
 * 
 */
public class BindGraph implements IInstruction{

	public BindGraph() {
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public CommandResult execute(IInvoker invoker, ICommandProxyNodeVisitable nodeCaller) throws Exception {
//		IStorage storage = invoker.getStorage();
//		UUID vertexCallerUUID = nodeCaller.getConcreteNodeUUID();
//		
//		Iterator<String>it = invoker.getParameterNameIterator();
//		while(it.hasNext()){
//			String paramName = it.next();
//			String paramValue = invoker.getParamValue(paramName);
//			
//			Vertex vertexCaller = storage.getVertex(vertexCallerUUID);
//			vertexCaller.setProperty(paramName, paramValue);
//		}
//		
//		storage.commit();
//		
//		CommandResult commandResult = new CommandResult(CommandResultType.Node);
//		commandResult.setNewNodeUUID(vertexCallerUUID);
//		commandResult.setOwnerUUID(vertexCallerUUID);
//		
//		return commandResult;
//	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandProxyNodeVisitable)
	 */
	@Override
	public IInstructionResult execute( IInstructionInvoker invoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable nodeCaller) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
