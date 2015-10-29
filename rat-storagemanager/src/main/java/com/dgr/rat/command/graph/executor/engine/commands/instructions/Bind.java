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

public class Bind implements IInstruction{
	
	public Bind() {

	}

	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		UUID vertexCallerUUID = nodeCaller.getStoredNodeUUID();
		Vertex outVertex = storage.getVertex(vertexCallerUUID);
		UUID edgeUUID = null;
		Iterator<String>it = invoker.getParameterNameIterator();
		while(it.hasNext()){
			String paramName = it.next();
			String paramValue = invoker.getParamValue(paramName);
			if(!Utils.isUUID(paramValue)){
				throw new Exception();
				// TODO log
			}
			Vertex inVertex = storage.getVertex(UUID.fromString(paramValue));
			
			edgeUUID = UUID.randomUUID();
			Edge edge = outVertex.addEdge(nodeCaller.getCommandName(), inVertex);
			edge.setProperty(RATConstants.EdgeUUIDField, edgeUUID.toString());
		}
		
		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(nodeCaller.getInMemoryNodeUUID());
		commandResult.setNewObjectUUID(edgeUUID);
		
		// COMMENT: i risultati dell'instruction bind non mi interessano...
		return commandResult;
	}
}
