/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.Set;
import java.util.UUID;

public interface ICommandNodeVisitable {
	public void accept(IGraphVisitor visitor, ICommandNodeVisitable parent)throws Exception;
	public Object getProperty(String propertyName);
	public Object getId();
	public boolean propertyExists(String propertyName);
	public UUID getInMemoryNodeUUID();
	public Set<String>getPropertyKeys();
	public boolean isExplored();
	public String getCommandName();
	public UUID getStoredNodeUUID();
	public void setRotNode(boolean isRotNode);
	public UUID getCommandGraphUUID();
	public boolean isRootNode();
	public ICommandNodeVisitable getParent();
}
