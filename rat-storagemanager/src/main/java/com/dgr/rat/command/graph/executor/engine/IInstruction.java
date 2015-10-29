/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;

public interface IInstruction {
	public IInstructionResult execute(final IInstructionInvoker invoker, final ICommandNodeVisitable nodeCaller) throws Exception;
}
