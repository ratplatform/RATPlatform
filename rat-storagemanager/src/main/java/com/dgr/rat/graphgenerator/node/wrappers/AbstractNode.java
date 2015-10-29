/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATFrame;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;

public abstract class AbstractNode<T extends IRATFrame> {
	private String _label = null; 
	private String _nodeContent = null;
	private UUID _nodeUUID = null;
	private VertexType _type = null;
	private String _commandOwner = null;
	private boolean _isAlreadyCreated = false;
	private T _node = null;
	
	public AbstractNode() {
	}
	
	public abstract void buildNodes(FramedGraph<Graph> framedGraph, String commandName, UUID commandUUID, String edgeName) throws Exception;
	
	public T getNode(){
		return _node;
	}
	
	protected void setNode(T node){
		_node = node;
	}
	
	/**
	 * @return the _rootNodeUUID
	 */
	public UUID get_nodeUUID() {
		return _nodeUUID;
	}

	/**
	 * @param _nodeUUID the _rootNodeUUID to set
	 */
	public void set_nodeUUID(UUID nodeUUID) {
		this._nodeUUID = nodeUUID;
	}

	/**
	 * @return the _nodeContent
	 */
	public String get_nodeContent() {
		return _nodeContent;
	}

	/**
	 * @param _rootNodeName the _nodeContent to set
	 */
	public void set_nodeContent(String content) {
		this._nodeContent = content;
	}

	/**
	 * @return the _label
	 */
	public String get_label() {
		return _label;
	}

	/**
	 * @param _label the _label to set
	 */
	public void set_label(String label) {
		this._label = label;
	}

	/**
	 * @return the type
	 */
	public VertexType getType() {
		return _type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(VertexType type) {
		this._type = type;
	}

	/**
	 * @return the _commandOwner
	 */
	public String get_commandOwner() {
		return _commandOwner;
	}

	/**
	 * @param _commandOwner the _commandOwner to set
	 */
	public void set_commandOwner(String commandOwner) {
		this._commandOwner = commandOwner;
	}

	/**
	 * @return the _isAlreadyCreated
	 */
	public boolean isAlreadyCreated() {
		return _isAlreadyCreated;
	}

	/**
	 * @param _isAlreadyCreated the _isAlreadyCreated to set
	 */
	public void set_isAlreadyCreated(boolean isAlreadyCreated) {
		this._isAlreadyCreated = isAlreadyCreated;
	}
}
