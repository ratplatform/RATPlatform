/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine;


public interface ICommandGraphData {
//	public <T extends ICommandGraphVisitable> void accept(AbstractCommandGraphVisitor visitor, Class<T> cls)throws Exception;
	public <T extends ICommandNodeVisitable> ICommandNodeVisitable getRootNode(Class<T> cls) throws Exception;
	public ICommandNodeVisitable getRootNode(ICommandGraphVisitableFactory factory) throws Exception;
//	public <T> T getData(Class<T> cls);
//	public String getJSON() throws IOException;
}
