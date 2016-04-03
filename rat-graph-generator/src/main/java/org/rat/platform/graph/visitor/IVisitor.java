package org.rat.platform.graph.visitor;

import org.rat.platform.command.nodes.CommandNode;

public interface IVisitor {
	public void visit(CommandNode node);
	public void addNodeAlreadyVisited(CommandNode node);
	public boolean alreadyVisited(CommandNode node);
}
