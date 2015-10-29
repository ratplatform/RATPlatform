/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeQueryPivotFrame;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;

public class QueryPivotNode extends AbstractNode<IRATNodeQueryPivotFrame>{
	private VertexType _towardNodeType = VertexType.Unknown;
	private VertexType _fromNodeType = VertexType.Unknown;
	private String _queryName = null;
	private String _startPipeInstruction = null;
    private String _internalPipeInstruction = null;
    private String _endPipeInstruction = null;
	
	public QueryPivotNode() {
		this.set_label(VertexType.QueryPivot.toString());
		this.setType(VertexType.QueryPivot);
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
			this.getNode().setTowardNode(_towardNodeType);
			this.getNode().setFromNode(_fromNodeType);
			this.getNode().setQueryName(_queryName);
			this.getNode().setStartPipeInstructionName(_startPipeInstruction);
			this.getNode().setInternalPipeInstruction(_internalPipeInstruction);
			this.getNode().setEndPipeInstruction(_endPipeInstruction);
			
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
	 * @return the _startPipeInstruction
	 */
	public String getStartPipeInstruction() {
		return _startPipeInstruction;
	}

	/**
	 * @param _startPipeInstruction the _startPipeInstruction to set
	 */
	public void setStartPipeInstruction(String startPipeInstruction) {
		this._startPipeInstruction = startPipeInstruction;
	}

	/**
	 * @return the _internalPipeInstruction
	 */
	public String geInternalPipeInstruction() {
		return _internalPipeInstruction;
	}

	/**
	 * @param _internalPipeInstruction the _internalPipeInstruction to set
	 */
	public void setInternalPipeInstruction(String internalPipeInstruction) {
		this._internalPipeInstruction = internalPipeInstruction;
	}

	/**
	 * @return the _endPipeInstruction
	 */
	public String getEndPipeInstruction() {
		return _endPipeInstruction;
	}

	/**
	 * @param _endPipeInstruction the _endPipeInstruction to set
	 */
	public void setEndPipeInstruction(String endPipeInstruction) {
		this._endPipeInstruction = endPipeInstruction;
	}
}
