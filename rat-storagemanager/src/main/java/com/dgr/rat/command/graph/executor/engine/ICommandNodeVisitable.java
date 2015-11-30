/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.Set;
import java.util.UUID;

// TODO ICommandNodeVisitable mi deve permettere di rappresentare anche il nodo equivalente salvato nel DB; per fare ciò devo trasferire 
// il modote di creazione del nodo al suo interno
// TODO: deve conoscere anche il graph di origine affinché si possano leggere le label degli edge
public interface ICommandNodeVisitable {
	public void accept(IGraphVisitor visitor, ICommandNodeVisitable parent)throws Exception;
	public Object getProperty(String propertyName);
	public Object getInMemoryNodeID();
//	public Object getStoredNodeID();
	public boolean propertyExists(String propertyName);
	public UUID getInMemoryNodeUUID();
	public Set<String>getPropertyKeys();
	public boolean isExplored();
	public String getCommandName();
	public UUID getStoredNodeUUID();
	public void setStoredNodeUUID(UUID uuid);
	public void setRotNode(boolean isRotNode);
	public UUID getCommandGraphUUID();
	public boolean isRootNode();
	public ICommandNodeVisitable getParent();
}
