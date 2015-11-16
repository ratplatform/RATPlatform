/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.queries;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.tinkerpop.blueprints.Graph;

public class QueryResult implements IInstructionResult{
	private Graph _graph = null;
	private UUID _inMemoryOwnerNodeUUID = null;
	private UUID _rootUUID = null;
	
	public QueryResult(UUID inMemoryOwnerNodeUUID) {
		_inMemoryOwnerNodeUUID = inMemoryOwnerNodeUUID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> cls) {
		return (T) this;
	}

	/**
	 * @return the _graph
	 */
	public Graph getGraph() {
		return _graph;
	}

	/**
	 * @param _graph the _graph to set
	 */
	public void setGraph(Graph graph) {
		this._graph = graph;
	}
	
	@Override
	public UUID getInMemoryOwnerNodeUUID() {
		return _inMemoryOwnerNodeUUID;
	}

	/**
	 * @return the _rootUUID
	 */
	public UUID getRootUUID() {
		return _rootUUID;
	}

	/**
	 * @param _rootUUID the _rootUUID to set
	 */
	public void setRootUUID(UUID rootUUID) {
		this._rootUUID = rootUUID;
	}
}
