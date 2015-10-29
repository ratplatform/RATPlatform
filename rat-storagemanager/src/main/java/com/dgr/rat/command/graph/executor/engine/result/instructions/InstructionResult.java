/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.instructions;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;

public class InstructionResult implements IInstructionResult{
	private UUID _storedNodeUUID = null;
	private UUID _inMemoryOwnerNodeUUID = null;
	
	public InstructionResult(UUID inMemoryOwnerNodeUUID) {
		_inMemoryOwnerNodeUUID = inMemoryOwnerNodeUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.IInstructionResult#getContent(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> cls) {
		// TODO Auto-generated method stub
		return (T)this;
	}

	/**
	 * @return the _storedNodeUUID
	 */
	public UUID getStoredNodeUUID() {
		return _storedNodeUUID;
	}

	/**
	 * @param _storedNodeUUID the _storedNodeUUID to set
	 */
	public void setNewObjectUUID(UUID newObjectUUID) {
		this._storedNodeUUID = newObjectUUID;
	}
	
	@Override
	public UUID getInMemoryOwnerNodeUUID() {
		return _inMemoryOwnerNodeUUID;
	}

}
