/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.queries;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class PipeResult implements IInstructionResult{
	private  GremlinPipeline<Vertex, Vertex> _content = null;
	private UUID _rootUUID = null;
	private UUID _inMemoryOwnerNodeUUID = null;
	
	public PipeResult(UUID inMemoryOwnerNodeUUID) {
		_inMemoryOwnerNodeUUID = inMemoryOwnerNodeUUID;
	}

	public void setContent(GremlinPipeline<Vertex, Vertex> content){
		_content = content;
	}
	
	public GremlinPipeline<Vertex, Vertex> getContent(){
		return _content;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.IInstructionResult#getContent(java.lang.Class)
	 */
	// COMMENT squallidissimo: da rivedere
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getContent(Class<T> cls) {
		// TODO Auto-generated method stub
		return (T) this;
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

	@Override
	public UUID getInMemoryOwnerNodeUUID() {
		return _inMemoryOwnerNodeUUID;
	}
}
