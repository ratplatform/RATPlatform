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
import com.tinkerpop.blueprints.Vertex;

public class ChangeProperty implements IInstruction{

	public ChangeProperty() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
//		// COMMENT: i parametri devono essere 2: il nodeUUID e il parametro da cambiare
//		if(invoker.getNumOfParameters() != 2){
//			// TODO log
//			throw new Exception();
//		}
		
		// COMMENT: il primo parametro deve essere sempre lo UUID del nodo
		String rootNodeUUID = invoker.getParamValueByIndex(0);
//		UUID inMemoryNodeUUID = nodeCaller.getInMemoryNodeUUID();
		if(!Utils.isUUID(rootNodeUUID)){
			// TODO log
			throw new Exception();
		}
		
		UUID rootUUID = UUID.fromString(rootNodeUUID);
		
		Vertex vertex = invoker.getStorage().getVertex(rootUUID);
		if(vertex == null){
			// TODO log
			throw new Exception();
		}
		
		// COMMENT: siccome il primo parametro è sempre lo UUID del nodo, parto da quello successivo.
		int num = invoker.getNumOfParameters();
		for (int inc = 1; inc < num; inc++){
			String paramName = invoker.getParamNameByIndex(inc);
			String paramValue = invoker.getNodeParamValue(paramName);
			
			Object param = vertex.getProperty(paramName);
			if(param == null){
				// TODO log
				throw new Exception();
			}
			vertex.setProperty(paramName, paramValue);
		}
		
//		IStorage storage = invoker.getStorage();
//		
//		Iterator<String>it = invoker.getParameterNameIterator();
//		while(it.hasNext()){
//			String paramName = it.next();
//			String paramValue = invoker.getNodeParamValue(paramName);
//			
//			
//			Object param = vertex.getProperty(paramName);
//			if(param == null){
//				// TODO log
//				throw new Exception();
//			}
//			vertex.setProperty(paramName, paramValue);
//		}
		
		InstructionResult commandResult = new InstructionResult(rootUUID);
		commandResult.setNewObjectUUID(rootUUID);
		invoker.addCommandResponse(nodeCaller, commandResult);
		
		return commandResult;
	}
}
