package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.storage.provider.IStorage;
import com.tinkerpop.blueprints.Vertex;

public class AddToIndex implements IInstruction{

	public AddToIndex() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		String fieldsToIndex = invoker.getNodeParamValue("fieldsToIndex");
		if(fieldsToIndex == null){
			throw new Exception();
			// TODO: log
		}
		
		UUID vertexUUID = nodeCaller.getStoredNodeUUID();
		Vertex vertex = storage.getVertex(vertexUUID);
		String[] fields = fieldsToIndex.split(",");
		for(String field : fields){
			Object value = vertex.getProperty(field);
			storage.addToIndex("rootverticesindex", vertex, field, value);
		}
		
		return null;
	}

}
