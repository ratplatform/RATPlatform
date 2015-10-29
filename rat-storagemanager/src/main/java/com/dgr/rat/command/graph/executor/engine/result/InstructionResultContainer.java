/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResultInfo;

// TODO: la parte dei result è un po' tutta da rivedere: non sono molto soddisfatto
public class InstructionResultContainer {
	private Map<InstructionResultInfo, IInstructionResult> _instructionResults = new HashMap<InstructionResultInfo, IInstructionResult>();
	
	public InstructionResultContainer() {
		// TODO Auto-generated constructor stub
	}

	public int getSize(){
		return _instructionResults.size();
	}
	
	public Iterator<InstructionResultInfo> getInstructionResultInfoIterator(){
		return _instructionResults.keySet().iterator();
	}
	
	public IInstructionResult pollWithInstructionResultInfo(InstructionResultInfo info){
		return _instructionResults.get(info);
	}

	public IInstructionResult pollByInMemoryUUID(UUID inMemoryUUID){
		IInstructionResult result = null;
		Iterator<InstructionResultInfo>it = _instructionResults.keySet().iterator();
		while (it.hasNext()){
			InstructionResultInfo info = it.next();
			UUID infoInMemoryUUID = info.getInMemoryOwnerNodeUUID();
			if(infoInMemoryUUID.toString().equalsIgnoreCase(inMemoryUUID.toString())){
				result = _instructionResults.remove(info);
				break;
			}
		}
		
		return result;
	}
	
	public IInstructionResult peekByInMemoryUUID(UUID inMemoryUUID){
		IInstructionResult result = null;
		Iterator<InstructionResultInfo>it = _instructionResults.keySet().iterator();
		while (it.hasNext()){
			InstructionResultInfo info = it.next();
			if(info.getInMemoryOwnerNodeUUID().toString().equalsIgnoreCase(inMemoryUUID.toString())){
				result = _instructionResults.get(info);
				break;
			}
		}
		
		return result;
	}

	public void setInstructionResult(InstructionResultInfo info, IInstructionResult instructionResult) {
		this._instructionResults.put(info, instructionResult);
	}
}
