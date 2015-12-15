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
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Vertex;

public class CreateWebDocument implements IInstruction{

	public CreateWebDocument() {
		// TODO Auto-generated constructor stub
	}
	
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
		
		if (!storage.vertexExists("webDocument", paramName, paramValue)){
			vertex = storage.addVertex(storedNodeUUID);
			storage.addToIndex("webDocument", vertex, paramName, paramValue);
			
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
			vertex = storage.getVertex("webDocument", paramName, paramValue);
			String uuid = vertex.getProperty(RATConstants.VertexUUIDField);
			if(Utils.isUUID(uuid)){
				storedNodeUUID = UUID.fromString(uuid);
				nodeCaller.setStoredNodeUUID(storedNodeUUID);
			}
			else{
				// TODO log
				throw new Exception();
			}
			
		}
//		storage.commit();
		
		InstructionResult commandResult = new InstructionResult(inMemoryNodeUUID);
		commandResult.setNewObjectUUID(storedNodeUUID);
		
		return commandResult;
	}
}
