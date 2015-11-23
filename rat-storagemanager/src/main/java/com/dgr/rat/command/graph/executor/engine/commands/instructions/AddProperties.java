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
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class AddProperties implements IInstruction{

	public AddProperties() {
		
	}
	
	// TODO: da sistemare affinche si possano aggiungere altre proprietà a questo nodo, o modificare quelle esistenti, senza crearlo nuovamente
	@Override
	public IInstructionResult execute(final IInstructionInvoker invoker, final ICommandNodeVisitable nodeCaller) throws Exception{
		// COMMENT: Siccome in questo caso il parametro è uno soltanto, non utilizzo la key della mappa 
//		String paramName = it.next();
//		paramValue = invoker.getNodeParamValue(paramName);
		IStorage storage = invoker.getStorage();
		
		UUID inMemoryNodeUUID = nodeCaller.getInMemoryNodeUUID();
		UUID storedNodeUUID = nodeCaller.getStoredNodeUUID();
		
		Vertex vertex = storage.addVertex(storedNodeUUID);
		Set<String> keys = nodeCaller.getPropertyKeys();
		Iterator<String>it = keys.iterator();
		while(it.hasNext()){
			String propertyName = it.next();
			Object propertyValue = null;
			
			propertyValue = nodeCaller.getProperty(propertyName);
			
			// COMMENT: l'uuid deve essere newVertexUUID e non quello del nodeOwner (vertexUUID)
			if(!propertyName.equalsIgnoreCase(RATConstants.VertexUUIDField)){
				vertex.setProperty(propertyName, propertyValue);	
			}
		}
		
		it = invoker.getParameterNameIterator();
		while(it.hasNext()){
			String paramName = it.next();
			String paramValue = invoker.getNodeParamValue(paramName);
			vertex.setProperty(paramName, paramValue);	
		}
		
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(inMemoryNodeUUID);
		commandResult.setNewObjectUUID(storedNodeUUID);
		
		return commandResult;
	}
}
