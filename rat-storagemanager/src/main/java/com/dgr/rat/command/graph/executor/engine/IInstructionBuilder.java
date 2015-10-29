/**
 * @author Daniele Grignani (dgr)
 * @date Oct 19, 2015
 */

package com.dgr.rat.command.graph.executor.engine;


public interface IInstructionBuilder {
	public IInstruction buildInstruction(String actionName) throws Exception;
}
