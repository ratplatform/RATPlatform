/**
 * @author Daniele Grignani (dgr)
 * @date Sep 27, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.Iterator;

public interface IInstructionNodeWrapper {
	public String getInstructionName();
	public ICommandNodeVisitable getCallerNode();
	public int getNumberOfInstructionParameters();
	public int getMaxNumParameters();
	public IInstructionParam getInstructionParameter(String paramName)  throws Exception;
	public Iterator<String> getInstructionParameterNameIterator();
}
