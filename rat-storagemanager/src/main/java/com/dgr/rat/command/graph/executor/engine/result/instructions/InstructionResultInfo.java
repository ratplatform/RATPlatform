/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.instructions;

import java.util.UUID;

public class InstructionResultInfo{
	private String _instructionName = null;
	private UUID _storedNodeUUID = null;
	private Object _storedNodeID = null;
	private UUID _inMemoryOwnerNodeUUID = null;
	
	public InstructionResultInfo(UUID inMemoryOwnerNodeUUID) {
		_inMemoryOwnerNodeUUID = inMemoryOwnerNodeUUID;
	}
	
	public String getInstructionName() {
		return _instructionName;
	}
	public void setInstructionName(String instructionName) {
		_instructionName = instructionName;
	}

	public UUID getStoredNodeUUID() {
		return _storedNodeUUID;
	}

	public void setStoredNodeUUID(UUID storedNodeUUID) {
		this._storedNodeUUID = storedNodeUUID;
	}
	
	public UUID getInMemoryOwnerNodeUUID() {
		return _inMemoryOwnerNodeUUID;
	}

	public Object getStoredNodeID() {
		return _storedNodeID;
	}

	public void setStoredNodeID(Object storedNodeID) {
		this._storedNodeID = storedNodeID;
	}
}
