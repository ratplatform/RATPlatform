/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IGraphVisitor;
import com.dgr.rat.command.graph.executor.engine.InstructionWrapper;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IQueryFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATQueryEdgeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.Utils;

public class QueryNodeWrapper implements ICommandNodeVisitable{
	private IQueryFrame _node;
	private List<ICommandNodeVisitable> _neighbors = new LinkedList<ICommandNodeVisitable>();
	private String _commandName = null;
	private boolean _explored = false;
	private ICommandNodeVisitable _parent = null;
	
	public QueryNodeWrapper(IQueryFrame node) {
		_node = node;
		_commandName = node.getVertexCommandOwnerField();
	}
	
	public ICommandNodeVisitable getParent(){
		return _parent;
	}
	
	protected IQueryFrame getVertex(){
		return _node;
	}
	
	public String getCommandName(){
		return _commandName;
	}


	public void accept(IGraphVisitor visitor, ICommandNodeVisitable parent) throws Exception {
		if(!this.isExplored()){
			_parent = parent;
			_explored = true;

			this.loadInstructions(visitor);
			this.loadNeighbors(visitor);
		}
	}
	
	private void loadInstructions(IGraphVisitor visitor) throws Exception{
		long num = _node.getNumberOfInstructions();
		for(int i = 0; i < num; i++){
			IInstructionNodeFrame instruction = _node.getInstruction(i);
			ICommandNodeVisitable invokable = new InstructionWrapper(instruction);
			visitor.visit(invokable, this);
		}
	}
	
	private void loadNeighbors(IGraphVisitor visitor) throws Exception{
		Iterator<IRATQueryEdgeFrame> it = _node.getAdjacentVertices().iterator();
		ICommandNodeVisitable visitable = null;
		while(it.hasNext()){
			IRATQueryEdgeFrame edge = it.next();
//			System.out.println(edgeUUID.toString());
			IQueryFrame neighbor = edge.getInRatQuery();
			String uuid = neighbor.getVertexUUIDField();
//			if(neighbor.getVertexTypeField().equals(VertexType.QueryPivot)){
//				throw new Exception();
//				// TODO log
//			}
			
			if(!Utils.isUUID(uuid)){
				throw new Exception();
				// TODO log
			}
			if(!visitor.nodeAlreadyExplored(UUID.fromString(uuid))){
				visitable = this.buildNode(neighbor);
				if(visitable == null){
					throw new Exception();
					// TODO log
				}
				_neighbors.add(visitable);
				visitor.visit(visitable, this);
			}
		}
	}
	
	private ICommandNodeVisitable buildNode(IQueryFrame ratNode) {
		ICommandNodeVisitable result = new QueryNodeWrapper(ratNode);
		return result;
	}

	public boolean isExplored() {
		return _explored;
	}

	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return _node.asVertex().getProperty(propertyName);
	}

	public UUID getInMemoryNodeUUID() {
		UUID uuID = null;
		String strUUID = this.getProperty(RATConstants.VertexUUIDField).toString();
		if(strUUID != null){
			uuID = UUID.fromString(strUUID);
		}
		return uuID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getId()
	 */
	@Override
	public Object getInMemoryNodeID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#propertyExists(java.lang.String)
	 */
	@Override
	public boolean propertyExists(String propertyName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getPropertyKeys()
	 */
	@Override
	public Set<String> getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getConcreteNodeUUID()
	 */
	// TODO da rivedere
	@Override
	public UUID getStoredNodeUUID() {
		// TODO Auto-generated method stub
//		throw new UnsupportedOperationException();
		return null;
	}
	
	@Override
	public void setStoredNodeUUID(UUID uuid) {
//		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#setRotNode(boolean)
	 */
	@Override
	public void setRotNode(boolean isRotNode) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getCommandGraphUUID()
	 */
	@Override
	public UUID getCommandGraphUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#isRootNode()
	 */
	@Override
	public boolean isRootNode() {
		// TODO Auto-generated method stub
		return _node.getIsRootVertexField();
	}
	
	@Override
	public VertexType getVertexType() {
		// TODO Auto-generated method stub
		return _node.getVertexTypeField();
	}
}
