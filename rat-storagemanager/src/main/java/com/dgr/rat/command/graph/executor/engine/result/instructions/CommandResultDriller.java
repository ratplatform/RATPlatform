/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.instructions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.IResultDriller;
import com.dgr.rat.commons.constants.RATConstants;

public class CommandResultDriller implements IResultDriller{
	private Map<String, Object> _commandResponsePropertiesMap = new HashMap<String, Object>();
	
	public CommandResultDriller() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.ICommandResultDriller#drill(com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResultInfo, com.dgr.rat.command.graph.executor.engine.result.IInstructionResult)
	 */
	@Override
	public void drill(CommandResponse commandResult) throws Exception {
		InstructionResultInfo info = commandResult.getInstructionResultInfo();
		if(info == null){
			throw new Exception();
			// TODO log ?
		}
		IInstructionResult result = commandResult.getInstructionResult();
		if(result == null){
			throw new Exception();
			// TODO log ?
		}
				
		InstructionResult instructionResult = result.getContent(InstructionResult.class);
		if(instructionResult == null){
			throw new Exception();
			// TODO log ?
		}
		
//		System.out.println("info.getInMemoryNodeUUID() " + info.getInMemoryOwnerNodeUUID());
//		System.out.println("info.getStoredNodeID() " + info.getStoredNodeID());
//		System.out.println("info.getStoredNodeUUID() " + info.getStoredNodeUUID());
//		System.out.println("info.getInstructionName() " + info.getInstructionName());
//		
//		System.out.println("instructionResult.getInMemoryOwnerNodeUUID() " + instructionResult.getInMemoryOwnerNodeUUID());
//		System.out.println("instructionResult.getStoredNodeUUID() " + instructionResult.getStoredNodeUUID());
		
		_commandResponsePropertiesMap.put(RATConstants.VertexUUIDField, instructionResult.getStoredNodeUUID());
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.ICommandResultDriller#getResult()
	 */
	@Override
	public Map<String, Object> getResult(){
		return _commandResponsePropertiesMap;
	}

	public Object getProperty(String key){
		return _commandResponsePropertiesMap.get(key);
	}
	
	public Set<String>getPropertyKeys(){
		return _commandResponsePropertiesMap.keySet();
	}
	
	public Iterator<String>getKeyIterator(){
		return this.getPropertyKeys().iterator();
	}
}
