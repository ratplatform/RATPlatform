/**
 * @author Daniele Grignani (dgr)
 * @date Oct 21, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;

public class InstructionWrapper implements ICommandNodeVisitable, IInstructionNodeWrapper{
	private IInstructionNodeFrame _instruction = null;
	private boolean _isAlreadyInvoked = false;
	private Map<String, InstructionParameterNodeWrapper> _neighbors = new LinkedHashMap<String, InstructionParameterNodeWrapper>();
	private ICommandNodeVisitable _caller = null;
	
	public InstructionWrapper(IInstructionNodeFrame instruction) {
		_instruction = instruction;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#accept(com.dgr.rat.queries.IGraphVisitor, com.dgr.rat.command.graph.ICommandProxyNodeVisitable)
	 */
	@Override
	public void accept(IGraphVisitor visitor, ICommandNodeVisitable caller) throws Exception {
		// COMMENT: tecnicamente il caller sarebbe il nodo parent, ossia il nodo al quale questo è collegato
		// Lo chiamo caller, anche se di fatto il caller di questa instruction sarebbe la classe IInstructionInvoker,
		// per distinguerlo dal parent degli altri nodi ICommandNodeVisitable, cioè quelli non instruction
		_caller = caller;
		if(!_isAlreadyInvoked){
			this.readInstructionParameters();
			
			visitor.invoke(this);
			
			_isAlreadyInvoked = true;
		}
	}
	
	private void readInstructionParameters() throws Exception{
		int num = (int) _instruction.getNumberOfInstructionParameters();
		for(int inc = 0; inc < num; inc++){
			IInstructionParameterNodeFrame neighbor = _instruction.getInstructionParameter(inc);
			InstructionParameterNodeWrapper instructionParameterNodeWrapper = new InstructionParameterNodeWrapper(neighbor);
			if(_neighbors.containsKey(neighbor.getVertexUserCommandsInstructionsParameterNameField())){
				throw new Exception();
				// TODO log
			}
			_neighbors.put(neighbor.getVertexUserCommandsInstructionsParameterNameField(), instructionParameterNodeWrapper);
		}
		
		if(_neighbors.size() != this.getNumberOfInstructionParameters()){
			System.out.println("_neighbors.size(): " + _neighbors.size());
			System.out.println("this.getNumberOfInstructionParameters(): " + this.getNumberOfInstructionParameters());
			throw new Exception();
			// TODO: log
		}
		
		if(_neighbors.isEmpty()){
			throw new Exception();
			// TODO: log
		}
	}
//	private void readInstructionParameters() throws Exception{
//		Iterator<IInstructionParameterNodeFrame> it = _instruction.getUserCommandsInstructionParameters().iterator();
//		while(it.hasNext()){
//			IInstructionParameterNodeFrame neighbor = it.next();
//			InstructionParameterNodeWrapper instructionParameterNodeWrapper = new InstructionParameterNodeWrapper(neighbor);
//			_neighbors.put(neighbor.getInstructionOrderField(), instructionParameterNodeWrapper);
//		}
//		
//		if(_neighbors.size() != this.getNumberOfInstructionParameters()){
//			throw new Exception();
//			// TODO: log
//		}
//		
//		if(_neighbors.isEmpty()){
//			throw new Exception();
//			// TODO: log
//		}
//	}
	
	public int getNumberOfInstructionParameters() {
		return (int) _instruction.getNumberOfInstructionParameters();
	}
	
//	protected boolean isAlreadyInvoked() {
//		return _isAlreadyInvoked;
//	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return _instruction.asVertex().getProperty(propertyName);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getId()
	 */
	@Override
	public Object getInMemoryNodeID() {
		// TODO Auto-generated method stub
		return _instruction.asVertex().getId();
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
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getProxyNodeUUID()
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
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getPropertyKeys()
	 */
	@Override
	public Set<String> getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#isExplored()
	 */
	@Override
	public boolean isExplored() {
		// TODO Auto-generated method stub
		return _isAlreadyInvoked;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getCommandName()
	 */
	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandProxyNodeVisitable#getConcreteNodeUUID()
	 */
	@Override
	public UUID getStoredNodeUUID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setStoredNodeUUID(UUID uuid) {
		throw new UnsupportedOperationException();
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
	 * @see com.dgr.rat.command.graph.instruction.IInstructionNodeWrapper#getInstructionName()
	 */
	@Override
	public String getInstructionName() {
		// TODO Auto-generated method stub
		return _instruction.getVertexContentField();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionNodeWrapper#getCallerNode()
	 */
	@Override
	public ICommandNodeVisitable getCallerNode() {
		// TODO Auto-generated method stub
		return _caller;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionNodeWrapper#getMaxNumParameters()
	 */
	@Override
	public int getMaxNumParameters() {
		// TODO Auto-generated method stub
		return _instruction.getMaxNumParameters();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionNodeWrapper#getInstructionParameter(int)
	 */
	@Override
	public IInstructionParam getInstructionParameter(String paramName) throws Exception {
		if(!_neighbors.containsKey(paramName)){
			throw new Exception();
			// TODO log
		}
		return _neighbors.get(paramName);
	}
//	public IInstructionParam getInstructionParameter(int num) throws Exception {
//		if(num > _neighbors.size()){
//			throw new Exception();
//			// TODO log
//		}
//		return _neighbors.get(num);
//	}

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
		return _caller;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstructionNodeWrapper#getInstructionParameterNameIterator()
	 */
	@Override
	public Iterator<String> getInstructionParameterNameIterator() {
		// TODO Auto-generated method stub
		return _neighbors.keySet().iterator();
	}

	@Override
	public VertexType getVertexType() {
		// TODO Auto-generated method stub
		return _instruction.getVertexTypeField();
	}

}
