package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.Iterator;
import java.util.List;
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
		String urlMD5 = Utils.getMD5(paramValue);
		if (!storage.vertexExists("webDocument", paramName, urlMD5)){
			vertex = storage.addVertex(storedNodeUUID);
			storage.addToIndex("webDocument", vertex, paramName, urlMD5);
			storage.addToIndex("rootverticesindex", vertex, RATConstants.VertexTypeField, vertex.getProperty(RATConstants.VertexTypeField));
			
			Set<String> keys = nodeCaller.getPropertyKeys();
			it = keys.iterator();
			while(it.hasNext()){
				String propertyName = it.next();
				Object propertyValue = null;
				
				if(propertyName.equalsIgnoreCase(RATConstants.VertexContentField)){
					propertyValue = urlMD5;
				}
				else if(propertyName.equalsIgnoreCase(RATConstants.VertexLabelField)){
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
			//vertex = storage.getVertex("webDocument", paramName, urlMD5);
			List<Vertex>list = storage.getVertices("webDocument", paramName, urlMD5);
			// COMMENT: poco sicuro, ma per ora fa lo stesso (do per scontato che la lista non sia vuota e che abbia un solo elemento)...
			vertex = list.get(0);
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
