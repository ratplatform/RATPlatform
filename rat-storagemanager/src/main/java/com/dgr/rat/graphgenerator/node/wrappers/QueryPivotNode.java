/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeQueryPivotFrame;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;

public class QueryPivotNode extends AbstractNode<IRATNodeQueryPivotFrame>{
	private VertexType _towardNodeType = VertexType.Unknown;
	private VertexType _fromNodeType = VertexType.Unknown;
	private String _queryName = null;
	private String _correlationKey = null;
	private int _orderField = 0;
	private boolean _isRoot = false;
//	private String _paramName = null;
	private List<String>_params = new LinkedList<String>();
	
	public QueryPivotNode(boolean isRoot) {
		this.set_label(VertexType.QueryPivot.toString());
		this.setType(VertexType.QueryPivot);
		_isRoot = isRoot;
	}
	
//	public String getParamName() {
//		return _paramName;
//	}

//	public void setParamName(String paramName) {
//		this._paramName = paramName;
//	}
	
	public List<String>getParams(){
		return _params;
	}
	
	public void setParamName(String paramName) throws Exception {
		if(_params.contains(paramName)){
			throw new Exception();
		}
		
		_params.add(paramName);
	}
	
	public void setTowardNode(VertexType type){
		_towardNodeType = type;
	}
	
	public VertexType getTowardNode(){
		return _towardNodeType;
	}
	
	public void setFromNode(VertexType vertexType){
		_fromNodeType = vertexType;
	}
	
	public VertexType getFromNode(){
		return _fromNodeType;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.node.wrappers.AbstractNode#buildNodes(com.tinkerpop.frames.FramedGraph, java.lang.String, java.util.UUID)
	 */
	@Override
	public void buildNodes(FramedGraph<Graph> framedGraph, String commandName, UUID commandUUID, String edgeName) throws Exception {
		if(!this.isAlreadyCreated()){
			this.setNode(framedGraph.addVertex(null, IRATNodeQueryPivotFrame.class));
			
			this.getNode().setVertexCommandOwnerField(edgeName);
			this.getNode().setVertexLabelField(this.get_label());
			this.getNode().setVertexTypeField(this.getType());
			this.getNode().setCommandGraphUUID(commandUUID.toString());
			this.getNode().setQueryName(_queryName);
			this.getNode().setInstructionOrderField(_orderField);
			this.getNode().setCorrelationKey(_correlationKey);
			this.getNode().setIsRootNode(_isRoot);
			
			UUID uuid = this.get_nodeUUID();
			if(uuid == null){
				System.out.println("uuid IS NULL");
				throw new Exception();
			}
			this.getNode().setVertexUUIDField(uuid.toString());
			this.set_isAlreadyCreated(true);
		}
	}

	/**
	 * @return the _queryName
	 */
	public String getQueryName() {
		return _queryName;
	}

	/**
	 * @param _queryName the _queryName to set
	 */
	public void setQueryName(String queryName) {
		this._queryName = queryName;
	}

	/**
	 * @return the _orderField
	 */
	public int get_orderField() {
		return _orderField;
	}

	/**
	 * @param _orderField the _orderField to set
	 */
	public void set_orderField(int orderField) {
		this._orderField = orderField;
	}

	/**
	 * @return the _correlationKey
	 */
	public String get_correlationKey() {
		return _correlationKey;
	}

	/**
	 * @param _correlationKey the _correlationKey to set
	 */
	public void set_correlationKey(String correlationKey) {
		this._correlationKey = correlationKey;
	}

	/**
	 * @return the _isRoot
	 */
	public boolean get_isRoot() {
		return _isRoot;
	}
}
