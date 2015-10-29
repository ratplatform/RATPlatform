/**
 * @author Daniele Grignani (dgr)
 * @date Oct 6, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result;

import java.util.UUID;

public interface IInstructionResult{
	public <T> T getContent(Class<T>cls);
	public UUID getInMemoryOwnerNodeUUID(); 
}
