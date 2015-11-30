/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IGraphVisitor;
import com.dgr.rat.command.graph.executor.engine.InstructionWrapper;
import com.dgr.rat.command.graph.executor.engine.commands.instructions.BindToParentSystemInstruction;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Vertex;

public class RATNodeProxy implements ICommandNodeVisitable{
	private IRATNodeFrame _node = null;
	private List<ICommandNodeVisitable> _neighbors = new LinkedList<ICommandNodeVisitable>();
	private String _commandName = null;
	private boolean _explored = false;
	private UUID _ratNodeUUID = UUID.randomUUID();
	private ICommandNodeVisitable _parent = null;
	
	public RATNodeProxy(IRATNodeFrame node){
		_node = node;
		_commandName =  _node.getVertexCommandOwnerField();
		System.out.println("Creo RATNodeProxy " + node.asVertex().getProperty(RATConstants.VertexContentField));
		System.out.println("Node degree " + _node.getVertexDegree());
	}
	
	public ICommandNodeVisitable getParent(){
		return _parent;
	}
	
	protected Vertex getVertex(){
		return _node.asVertex();
	}
	
	public String getCommandName(){
		return _commandName;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#accept(com.dgr.rat.command.decorator.IVisitor)
	 */
	@Override
	public void accept(IGraphVisitor visitor, ICommandNodeVisitable parent) throws Exception {
		if(!this.isExplored()){
			_parent = parent;
			_explored = true;
			
			this.loadInstructions(visitor, parent);
			this.loadNeighbors(visitor);
		}
	}
	
	private void loadInstructions(IGraphVisitor visitor, ICommandNodeVisitable parent) throws Exception{
		long num = _node.getNumberOfInstructions();
		for(int i = 0; i < num; i++){
			IInstructionNodeFrame instruction = _node.getInstruction(i);
			ICommandNodeVisitable invokable = new InstructionWrapper(instruction);
			visitor.visit(invokable, this);
			
//			System.out.println(instruction.getVertexContentField());
//			System.out.println(instruction.getVertexLabelField());
//			System.out.println(instruction.getVertexUUIDField());
//			System.out.println(order);
		}
		
		// COMMENT Eseguo il bind tra questo nodo e quello precedente, ossia quello al quale è legato
		if(parent != null){
			ICommandNodeVisitable invokable = new BindToParentSystemInstruction(parent);
			visitor.visit(invokable, this);
		}
	}
	
	private ICommandNodeVisitable buildNode(IRATNodeFrame ratNode) {
		ICommandNodeVisitable result = new RATNodeProxy(ratNode);
		return result;
	}
	
	private void loadNeighbors(IGraphVisitor visitor) throws Exception{
		Iterator<IRATNodeEdgeFrame> it = _node.getAdjacentVertices().iterator();
		ICommandNodeVisitable commandVertexWrapper = null;
		while(it.hasNext()){
			IRATNodeEdgeFrame edge = it.next();
//			System.out.println(edgeUUID.toString());
			IRATNodeFrame neighbor = edge.getInRatNode();
			String uuid = neighbor.getVertexUUIDField();
			if(neighbor.getVertexTypeField().equals(VertexType.QueryPivot)){
				continue;
			}
			
			if(!Utils.isUUID(uuid)){
				throw new Exception();
				// TODO log
			}
			if(!visitor.nodeAlreadyExplored(UUID.fromString(uuid))){
				commandVertexWrapper = this.buildNode(neighbor);
				if(commandVertexWrapper == null){
					throw new Exception();
					// TODO log
				}
				_neighbors.add(commandVertexWrapper);
				visitor.visit(commandVertexWrapper, this);
			}
		}
	}
	
	public boolean isExplored() {
		return _explored;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#getProperty()
	 */
	@Override
	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return _node.asVertex().getProperty(propertyName);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#getId()
	 */
	@Override
	public Object getInMemoryNodeID() {
		// TODO Auto-generated method stub
		return _node.asVertex().getId();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#propertyExists(java.lang.String)
	 */
	@Override
	public boolean propertyExists(String propertyName) {
		// TODO: non dovrebbe mai accadere, ma la proprietà potrebbe esistere ed essere settata a "null" oppure vuota.
		// In futuro gestire questi casi.
		String result = this.getProperty(propertyName).toString();
		return result != null ? true : false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#getUUID()
	 */
	@Override
	public UUID getInMemoryNodeUUID() {
		UUID uuID = null;
		String strUUID = this.getProperty(RATConstants.VertexUUIDField).toString();
		if(strUUID != null){
			uuID = UUID.fromString(strUUID);
		}
		return uuID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.decorator.IVisitable#getPropertyKeys()
	 */
	@Override
	public Set<String> getPropertyKeys() {
		return _node.asVertex().getPropertyKeys();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandGraphVisitable#getConcreteUUID()
	 */
	// COMMENT: rappresenta l'UUID del vertex creato nel DB
	@Override
	public UUID getStoredNodeUUID() {
		return _ratNodeUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#setRotNode(boolean)
	 */
	@Override
	public void setRotNode(boolean isRotNode) {
		_node.setIsRootVertexField(isRotNode);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getGraphCommandUUID()
	 */
	@Override
	public UUID getCommandGraphUUID() {
		UUID uuID = null;
		String strUUID = this.getProperty(RATConstants.CommandGraphUUID).toString();
		if(strUUID != null){
			uuID = UUID.fromString(strUUID);
		}
		return uuID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#isRootNode()
	 */
	@Override
	public boolean isRootNode() {
		// TODO Auto-generated method stub
		return _node.getIsRootVertexField();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#setStoredNodeUUID(java.util.UUID)
	 */
	@Override
	public void setStoredNodeUUID(UUID uuid) {
		_ratNodeUUID = uuid;
	}
}
