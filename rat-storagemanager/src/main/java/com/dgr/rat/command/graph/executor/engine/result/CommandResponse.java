/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResultInfo;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.StatusCode;

// TODO: la parte dei result è un po' tutta da rivedere: non sono molto soddisfatto
public class CommandResponse {
	private IInstructionResult _instructionResult = null;
	private InstructionResultInfo _instructionResultInfo = null;
	
	private StatusCode _statusCode = StatusCode.Unknown;
	private String _commandName = null;
	private UUID _commandUUID = null;
	private JSONType _commandType = null;
	private String _commandVersion = null;
	private IResultDriller _driller = null;

	public CommandResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public Object getProperty(String key){
		return _driller.getProperty(key);
	}
	
	public Set<String>getPropertyKeys(){
		return _driller.getPropertyKeys();
	}
	
	public Iterator<String>getKeyIterator(){
		return this.getPropertyKeys().iterator();
	}
	
	public Map<String, Object> getResult(){
		return _driller.getResult();
	}
	
	public void drill(IResultDriller driller) throws Exception{
		_driller = driller;
		_driller.drill(this);
	}
	
	public String getCommandName() {
		return _commandName;
	}
	
	public void setCommandName(String commandName) {
		_commandName = commandName;
	}
	
	public void setStatusCode(StatusCode statusCode) {
		_statusCode = statusCode;
	}
	public StatusCode getStatusCode() {
		return _statusCode;
	}

	public void setInstructionResult(InstructionResultInfo info, IInstructionResult instructionResult) {
		_instructionResultInfo = info;
		_instructionResult = instructionResult;
	}
	
	public InstructionResultInfo getInstructionResultInfo(){
		return _instructionResultInfo;
	}
	
	public IInstructionResult getInstructionResult(){
		return _instructionResult;
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
	
	public void setCommandUUID(UUID commandUUID) {
		this._commandUUID = commandUUID;
	}

	public UUID getCommandUUID() {
		return _commandUUID;
	}
}
