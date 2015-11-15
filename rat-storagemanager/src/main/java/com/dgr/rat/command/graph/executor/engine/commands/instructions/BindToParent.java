/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.Iterator;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class BindToParent implements IInstruction{
	
	public BindToParent() {

	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IAction#execute()
	 */
	@Override
	public IInstructionResult execute(final IInstructionInvoker invoker, final ICommandNodeVisitable nodeCaller) throws Exception{
		System.out.println("Enter in bindtoparent");
		// COMMENT: Siccome in questo caso il parametro è uno soltanto, non utilizzo la key della mappa 
		Iterator<String>it = invoker.getParameterNameIterator();
		String paramName = it.next();
		String paramValue = invoker.getNodeParamValue(paramName);
		if(!Utils.isUUID(paramValue)){
			throw new Exception();
			// TODO log
		}
		
		IStorage storage = invoker.getStorage();
		
		UUID vertexUUID = nodeCaller.getStoredNodeUUID();
		Vertex inVertex = storage.getVertex(UUID.fromString(paramValue));
		Vertex outVertex = storage.getVertex(vertexUUID);
		
		UUID edgeUUID = UUID.randomUUID();
		Edge edge = inVertex.addEdge(nodeCaller.getCommandName(), outVertex);
		edge.setProperty(RATConstants.EdgeUUIDField, edgeUUID.toString());
		edge.setProperty(RATConstants.CommandGraphUUID, nodeCaller.getCommandGraphUUID());
		
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(nodeCaller.getInMemoryNodeUUID());
		commandResult.setNewObjectUUID(edgeUUID);
		
		System.out.println("Exit from bindtoparent");
		// COMMENT: i risultati dell'instruction bindToParent non mi interessano...
		return commandResult;
	}
}
