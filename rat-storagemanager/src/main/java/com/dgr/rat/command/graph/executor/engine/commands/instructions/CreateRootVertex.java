/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.storage.provider.IStorage;
import com.tinkerpop.blueprints.Vertex;

public class CreateRootVertex implements IInstruction{

	public CreateRootVertex() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable)
	 */
	// COMMENT: Qui viene creato il root vertex,  salvati nel DB, del comando
	@Override
	public IInstructionResult execute(final IInstructionInvoker invoker, final ICommandNodeVisitable nodeCaller) throws Exception{
		// COMMENT: Siccome in questo caso il parametro è uno soltanto, non utilizzo la key della mappa 
		Iterator<String>it = invoker.getParameterNameIterator();
		String paramName = it.next();
		String paramValue = invoker.getNodeParamValue(paramName);
		IStorage storage = invoker.getStorage();
		
		UUID inMemoryNodeUUID = nodeCaller.getInMemoryNodeUUID();
		UUID storedNodeUUID = nodeCaller.getStoredNodeUUID();
		Vertex vertex = null;
		
		// TODO: in realtà è un errore: lo storedNodeUUID viene creato 
		// quando viene creato il nodo inMemory (RATNodeProxy): da correggere aggiungendo un
		// attributo di tipo hash del nome ed un altro che conservi  anche l'inMemoryNodeUUID
		// la ricerca andrà fatta sia sull'hash del nome che sull'inMemoryNodeUUID, se possibile, altrimenti
		// eliminare vertexExists (col rischio di creare nodi doppi: cercare di evitarlo)
		if (!storage.vertexExists(storedNodeUUID)){
			vertex = storage.addVertex(storedNodeUUID);
			Set<String> keys = nodeCaller.getPropertyKeys();
			it = keys.iterator();
			while(it.hasNext()){
				String propertyName = it.next();
				Object propertyValue = null;
				
				if(propertyName.equalsIgnoreCase(RATConstants.VertexContentField) ||
						propertyName.equalsIgnoreCase(RATConstants.VertexLabelField)){
					propertyValue = paramValue;
				}
				else{
					propertyValue = nodeCaller.getProperty(propertyName);
				}
				
				// COMMENT: l'uuid deve essere newVertexUUID e non quello del nodeOwner (vertexUUID)
				if(!propertyName.equalsIgnoreCase(RATConstants.VertexUUIDField)){
					vertex.setProperty(propertyName, propertyValue);	
				}
			}
		}
		else{
			vertex = storage.getVertex(storedNodeUUID);
			// TODO log
		}
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(inMemoryNodeUUID);
		commandResult.setNewObjectUUID(storedNodeUUID);
		
		invoker.addCommandResponse(nodeCaller, commandResult);
		
		return commandResult;
	}
}
