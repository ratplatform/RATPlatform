/**
 * @author Daniele Grignani (dgr)
 * @date Oct 4, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IGraphVisitor;
import com.dgr.rat.command.graph.executor.engine.IInstructionNodeWrapper;
import com.dgr.rat.command.graph.executor.engine.IInstructionParam;
import com.dgr.rat.json.utils.ReturnType;

public class BindToParentSystemInstruction implements ICommandNodeVisitable, IInstructionNodeWrapper{
	private final static String BindToParent = "BindToParent";
	private boolean _isAlreadyInvoked = false;
	private ICommandNodeVisitable _parentNode = null;
	private Map<Integer, IInstructionParam> _neighbors = new HashMap<Integer, IInstructionParam>();
	private ICommandNodeVisitable _caller = null;
	private UUID _nodeUUID = UUID.randomUUID();
	
	public BindToParentSystemInstruction(ICommandNodeVisitable parentNode) {
		_parentNode = parentNode;
	}
	
	@Override
	public void accept(IGraphVisitor visitor, ICommandNodeVisitable callerNode) throws Exception {
		// COMMENT: tecnicamente il caller sarebbe il nodo parent, ossia il nodo al quale questo è collegato
		// Lo chiamo caller, anche se di fatto il caller di questa instruction sarebbe la classe IInstructionInvoker,
		// per distinguerlo dal parent degli altri nodi ICommandNodeVisitable, cioè quelli non instruction
		_caller = callerNode;
		
		if(!_isAlreadyInvoked){

//			COMMENT: qui devo aggiungere il parametro costituito dall'edge ricavato ProxyRATNode.loadNeighbors che connette _ownerNode con _parentNode
//			in ProxyRATNode.loadNeighbors l'edgeCommandUUID lo metto in una mappa (Neighbor : parentEdgeUUID) e qui recupero l'edgeuuid
//			che inserisco in un altro parametro con un nome specifico. I due parametri poi li prendo per nome in BindToParent
			BindToParentParameter bindToParentParameter = new BindToParentParameter();
			bindToParentParameter.setParameter("parentUUID", _parentNode.getStoredNodeUUID().toString());
			bindToParentParameter.setParameterUUID(UUID.randomUUID().toString());
			bindToParentParameter.setReturnType(ReturnType.uuid);
			_neighbors.put(0, bindToParentParameter);
			
			visitor.invoke(this);
			
			_isAlreadyInvoked = true;
		}
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvokable#getInstructionName()
	 */
	@Override
	public String getInstructionName() {
		// TODO Auto-generated method stub
		return BindToParentSystemInstruction.BindToParent;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvokable#getOwnerNode()
	 */
	@Override
	public ICommandNodeVisitable getCallerNode() {
		return _caller;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvokable#getNumberOfInstructionParameters()
	 */
	@Override
	public int getNumberOfInstructionParameters() {
		// TODO Auto-generated method stub
		return _neighbors.size();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvokable#getMaxNumParameters()
	 */
	@Override
	public int getMaxNumParameters() {
		// TODO Auto-generated method stub
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvokable#getInstructionParameter(int)
	 */
	@Override
	public IInstructionParam getInstructionParameter(int num) throws Exception {
		if(num > _neighbors.size()){
			throw new Exception();
			// TODO log
		}
		return _neighbors.get(num);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyName) {
		return this.getClass().toString();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getId()
	 */
	@Override
	public Object getId() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#propertyExists(java.lang.String)
	 */
	@Override
	public boolean propertyExists(String propertyName) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getProxyNodeUUID()
	 */
	@Override
	public UUID getInMemoryNodeUUID() {
		return _nodeUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getPropertyKeys()
	 */
	@Override
	public Set<String> getPropertyKeys() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#isExplored()
	 */
	@Override
	public boolean isExplored() {
		// TODO Auto-generated method stub
		return _isAlreadyInvoked;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getCommandName()
	 */
	@Override
	public String getCommandName() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getRATNodeUUID()
	 */
	@Override
	public UUID getStoredNodeUUID() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setStoredNodeUUID(UUID uuid) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#setRotNode(boolean)
	 */
	@Override
	public void setRotNode(boolean isRotNode) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getCommandGraphUUID()
	 */
	@Override
	public UUID getCommandGraphUUID() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#isRootNode()
	 */
	@Override
	public boolean isRootNode() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable#getParent()
	 */
	@Override
	public ICommandNodeVisitable getParent() {
		// TODO Auto-generated method stub
		return _parentNode;
	}
}
