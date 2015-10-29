/**
 * @author Daniele Grignani (dgr)
 * @date Sep 27, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

public interface IInstructionNodeWrapper {
	public String getInstructionName();
	public ICommandNodeVisitable getCallerNode();
//	public String getCallerNodeName();
//	public String getCallerNodeUUID();
//	public String getInstructionUUID();
	public int getNumberOfInstructionParameters();
	public int getMaxNumParameters();
	public IInstructionParam getInstructionParameter(int num)  throws Exception;
}
