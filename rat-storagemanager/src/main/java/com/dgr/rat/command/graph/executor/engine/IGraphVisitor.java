/**
 * @author Daniele Grignani (dgr)
 * @date Oct 20, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.UUID;

public interface IGraphVisitor {
	public ICommandNodeVisitable getExplored(UUID uuid);
	public boolean nodeAlreadyExplored(UUID uuid);
	public void addExploredNode(ICommandNodeVisitable node) throws Exception;
	public void visit(ICommandNodeVisitable visitable, ICommandNodeVisitable parent) throws Exception;
	public RemoteCommandContainer getParameters();
	public void invoke(IInstructionNodeWrapper instruction) throws Exception;
}
