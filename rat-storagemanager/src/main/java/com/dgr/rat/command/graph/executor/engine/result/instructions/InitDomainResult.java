/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.instructions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;

public class InitDomainResult implements IInstructionResult{
	private Map<String, UUID>_map = new HashMap<String, UUID>();
	private UUID _inMemoryOwnerNodeUUID = null;
	
	public InitDomainResult(UUID inMemoryOwnerNodeUUID) {
		_inMemoryOwnerNodeUUID = inMemoryOwnerNodeUUID;
	}
	
	public UUID getUUID(String key){
		return _map.get(key);
	}
	
	public void setUUID(String key, UUID uuid){
		_map.put(key, uuid);
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

	@Override
	public UUID getInMemoryOwnerNodeUUID() {
		return _inMemoryOwnerNodeUUID;
	}
}
