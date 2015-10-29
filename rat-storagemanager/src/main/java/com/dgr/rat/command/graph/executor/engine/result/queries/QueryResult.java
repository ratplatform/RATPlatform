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
}
