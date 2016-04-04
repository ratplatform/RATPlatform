/**
 * @author: Daniele Grignani
 * @date: Nov 9, 2015
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

public class CreateRootPlatformVertex implements IInstruction{

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(final IInstructionInvoker invoker, final ICommandNodeVisitable nodeCaller) throws Exception{
		// COMMENT: Siccome in questo caso il parametro è uno soltanto, non utilizzo la key della mappa 
		Iterator<String>it = invoker.getParameterNameIterator();
		String paramName = it.next();
		String paramValue = invoker.getNodeParamValue(paramName);
		IStorage storage = invoker.getStorage();
		
		UUID inMemoryNodeUUID = nodeCaller.getInMemoryNodeUUID();
		nodeCaller.setStoredNodeUUID(inMemoryNodeUUID);
		Vertex vertex = null;
		
		// COMMENT: qui è l'unico posto dove vertexExists ha senso: l'UUID del nodo root della piattaforma è sempre uguale  
		if (!storage.vertexExists(inMemoryNodeUUID)){
			vertex = storage.addVertex(inMemoryNodeUUID);
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
			
			storage.addToIndex("rootverticesindex", vertex, RATConstants.VertexTypeField, vertex.getProperty(RATConstants.VertexTypeField));
		}
		else{
			// TODO: non deve essere chiamato due volte; se lo fosse, le istruzioni successive non devono essere eseguite
			// ed il grafo deve interrompere l'esecuzione di sé stesso (cosa che ora non avviene)
			vertex = storage.getVertex(inMemoryNodeUUID);
			// TODO log
		}
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(inMemoryNodeUUID);
		commandResult.setNewObjectUUID(inMemoryNodeUUID);
		
		invoker.addCommandResponse(nodeCaller, commandResult);
		
		return commandResult;
	}

}
