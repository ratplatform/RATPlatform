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
import com.dgr.utils.StringUtils;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Vertex;

public class DeleteNode implements IInstruction{

	public DeleteNode() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		// COMMENT: i parametri devono essere 3: il nodeUUID, parentNodeUUID e il parametro da cambiare
		if(invoker.getNumOfParameters() != 3){
			// TODO log
			throw new Exception();
		}
		
		// COMMENT: il primo parametro deve essere sempre lo UUID del nodo
		String nodeUUIDStr = invoker.getParamValueByIndex(0);
		String parentNodeUUIDStr = invoker.getParamValueByIndex(1);
		String statusStr = invoker.getParamValueByIndex(2);

		if(!Utils.isUUID(nodeUUIDStr)){
			// TODO log
			throw new Exception();
		}
		if(!Utils.isUUID(parentNodeUUIDStr)){
			// TODO log
			throw new Exception();
		}
		if(!StringUtils.isParsableToBool(statusStr)){
			// TODO log
			throw new Exception();
		}
		
		UUID nodeUUID = UUID.fromString(nodeUUIDStr);
		Vertex vertex = invoker.getStorage().getVertex(nodeUUID);
		if(vertex == null){
			// TODO log
			throw new Exception();
		}
		
		UUID parentNodeUUID = UUID.fromString(parentNodeUUIDStr);
		Vertex parentVertex = invoker.getStorage().getVertex(parentNodeUUID);
		if(parentVertex == null){
			// TODO log
			throw new Exception();
		}
		
		boolean status = Boolean.parseBoolean(statusStr);
		Object param = vertex.getProperty(RATConstants.IsDeleted);
		if(param == null){
			// TODO log
			throw new Exception();
		}
		vertex.setProperty(RATConstants.IsDeleted, statusStr);
		
		Object subNodes = parentVertex.getProperty(RATConstants.SubNodes);
		if(subNodes != null){
			int num = Integer.parseInt(subNodes.toString());
			if(status == true){
				if(num > 0){
					--num;
				}
			}
			else{
				++num;
			}
			parentVertex.setProperty(RATConstants.SubNodes, num);
		}
		
		InstructionResult commandResult = new InstructionResult(nodeUUID);
		commandResult.setNewObjectUUID(nodeUUID);
		invoker.addCommandResponse(nodeCaller, commandResult);
		
		return commandResult;
	}
}
