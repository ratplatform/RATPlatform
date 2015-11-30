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
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class BindDomainUser implements IInstruction{
	
	public BindDomainUser() {

	}
	
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		UUID vertexCallerUUID = nodeCaller.getStoredNodeUUID();
		Vertex outVertex = storage.getVertex(vertexCallerUUID);
		UUID edgeUUID = UUID.randomUUID();;
		
		Iterator<String>it = invoker.getParameterNameIterator();
		while(it.hasNext()){
			String paramName = it.next();
			String paramValue = invoker.getNodeParamValue(paramName);
			if(!Utils.isUUID(paramValue)){
				throw new Exception();
				// TODO log
			}
			edgeUUID = UUID.randomUUID();
			Edge edge = null;
			Vertex inVertex = storage.getVertex(UUID.fromString(paramValue));
			VertexType inVertexType = VertexType.fromString(inVertex.getProperty(RATConstants.VertexTypeField).toString());
			if(VertexType.compare(inVertexType, VertexType.User)){
				edge = outVertex.addEdge(nodeCaller.getCommandName(), inVertex);
			}
			else{
				edge = inVertex.addEdge(nodeCaller.getCommandName(), outVertex);
			}
			outVertex.setProperty(RATConstants.VertexIsRootField, false);

			edge.setProperty(RATConstants.EdgeUUIDField, edgeUUID.toString());
		}
		
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(nodeCaller.getInMemoryNodeUUID());
		commandResult.setNewObjectUUID(edgeUUID);
		
		// COMMENT: i risultati dell'instruction bind non mi interessano...
		return commandResult;
	}
}
