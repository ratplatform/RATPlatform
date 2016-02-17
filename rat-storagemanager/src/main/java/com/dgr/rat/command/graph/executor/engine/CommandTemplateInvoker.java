/**
 * @author Daniele Grignani (dgr)
 * @date Oct 20, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.IResultDriller;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;

public class CommandTemplateInvoker implements ICommandTemplateInvoker, IGraphVisitor{
	private String _commandName = null;
	private UUID _commandUUID = null;
	private JSONType _commandType = null;
	private String _commandVersion = null;
	private CommandData _dataWrapper = null;
	private Map<UUID, ICommandNodeVisitable> _explored = new LinkedHashMap<UUID, ICommandNodeVisitable>();
	private IInstructionInvoker _invoker = null;
	private IResultDriller _driller = null;
	
	public CommandTemplateInvoker(CommandData dataWrapper, IResultDriller driller) {
		_dataWrapper = dataWrapper;	
		_driller = driller;
	}

	@Override
	public CommandResponse execute(ICommandGraphVisitableFactory visitableFactory, IInstructionInvoker invoker) throws Exception {
		ICommandNodeVisitable rootNode = visitableFactory.buildNode(_dataWrapper);
		_invoker = invoker;
		
		this.visit(rootNode, null);
		
		// COMMENT: voglio un solo risultato, ed è quello del nodo root del comando
		CommandResponse commandResponse = _invoker.getCommandResponse();
		if(commandResponse == null){
			throw new Exception();
			// TODO log
		}
		commandResponse.setCommandName(_commandName);
		commandResponse.setCommandType(_commandType);
		commandResponse.setCommandUUID(_commandUUID);
		commandResponse.setCommandVersion(_commandVersion);
		commandResponse.setStatusCode(StatusCode.Ok);
		commandResponse.drill(_driller);
		
		return commandResponse;
	}
	
	public ICommandNodeVisitable getExplored(UUID uuid){
		ICommandNodeVisitable result = null;
		if(_explored.containsKey(uuid)){
			result = _explored.get(uuid);
		}
		
		return result;
	}

	public boolean nodeAlreadyExplored(UUID uuid){
		return _explored.containsKey(uuid);
	}

	public void addExploredNode(ICommandNodeVisitable node) throws Exception{
		UUID uuid = node.getInMemoryNodeUUID();
		if(this.nodeAlreadyExplored(uuid)){
			throw new Exception();
			// TODO log
		}
		
		_explored.put(uuid, node);
	}

	public void visit(ICommandNodeVisitable visitable, ICommandNodeVisitable parent) throws Exception {
		//System.out.println("Sto esplorando " + visitable.getProperty(RATConstants.VertexContentField));
		
		if(!visitable.isExplored()){
			this.addExploredNode(visitable);
		}
		
		visitable.accept(this, parent);
	}
	
	/************************************************************************************************************/
	
	public void setCommandUUID(UUID commandUUID) {
		this._commandUUID = commandUUID;
	}

	public UUID getCommandUUID() {
		return _commandUUID;
	}

	public String getCommandName() {
		return _commandName;
	}

	public void setCommandName(String commandName) {
		this._commandName = commandName;
	}

	public JSONType getCommandType() {
		return _commandType;
	}

	public void setCommandType(JSONType commandType) {
		this._commandType = commandType;
	}

	public String getCommandVersion() {
		return _commandVersion;
	}

	public void setCommandVersion(String commandVersion) {
		this._commandVersion = commandVersion;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.queries.IGraphVisitor#getParameters()
	 */
	@Override
	public RemoteCommandContainer getParameters() {
		// TODO Auto-generated method stub
		return _dataWrapper.getParameters();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.queries.IGraphVisitor#invoke(com.dgr.rat.command.graph.instruction.IInstructionNodeWrapper)
	 */
	@Override
	public void invoke(IInstructionNodeWrapper instruction) throws Exception {
		_invoker.invoke(instruction);
	}
}
