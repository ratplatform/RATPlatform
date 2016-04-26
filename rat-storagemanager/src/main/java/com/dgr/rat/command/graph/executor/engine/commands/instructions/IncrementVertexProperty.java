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

// trash
public class IncrementVertexProperty implements IInstruction{

	public IncrementVertexProperty() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		UUID vertexCallerUUID = nodeCaller.getStoredNodeUUID();
		
		Iterator<String>it = invoker.getParameterNameIterator();
		while(it.hasNext()){
			String paramName = it.next();
			//String paramValue = invoker.getNodeParamValue(paramName);
			
			Vertex vertexCaller = storage.getVertex(vertexCallerUUID);
			//vertexCaller.setProperty(paramName, paramValue);
			
			Object subNodes = vertexCaller.getProperty(paramName);
			if(subNodes == null){
				vertexCaller.setProperty(paramName, 1);
			}
			else{
				int num = Integer.parseInt(subNodes.toString());
				vertexCaller.setProperty(paramName, ++num);
			}
		}
		
//		storage.commit();

		InstructionResult commandResult = new InstructionResult(nodeCaller.getInMemoryNodeUUID());
		commandResult.setNewObjectUUID(vertexCallerUUID);
		
		// COMMENT: i risultati dell'instruction SetVertexProperty non mi interessano...
		return commandResult;
	}

}
