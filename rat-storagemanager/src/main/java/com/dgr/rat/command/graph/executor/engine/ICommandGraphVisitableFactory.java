/**
 * @author Daniele Grignani (dgr)
 * @date Sep 13, 2015
 */

package com.dgr.rat.command.graph.executor.engine;


public interface ICommandGraphVisitableFactory {
	public ICommandGraphData buildGraph(CommandData commandData) throws Exception;
	public ICommandNodeVisitable buildNode (CommandData commandData) throws Exception;
}
