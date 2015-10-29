/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;

// COMMENT: ATTENZIONE: GetAllDomains, SetQueryPipe e ExecuteQueryPipe operano in cascata e lavorano come se fossero una pipeline
public class GetAllDomains implements IInstruction{

	public GetAllDomains() {
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		return QueryPipeHelpers.executePipe(invoker, nodeCaller);
	}
}
