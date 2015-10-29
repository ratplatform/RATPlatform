/**
 * @author Daniele Grignani (dgr)
 * @date Oct 4, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import com.dgr.rat.command.graph.executor.engine.IInstructionParam;
import com.dgr.rat.json.utils.ReturnType;

public class BindToParentParameter implements IInstructionParam{
	private String _parameterName = null;
	private String _parameterValue = null;
	private String _parameterUUID = null;
	private ReturnType _returnType = ReturnType.unknown;
	
	public BindToParentParameter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getInstructionsParameterNameField()
	 */
	@Override
	public String getInstructionsParameterNameField() {
		// TODO Auto-generated method stub
		return _parameterName;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getInstructionsParameterValueField()
	 */
	@Override
	public String getInstructionsParameterValueField() {
		// TODO Auto-generated method stub
		return _parameterValue;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getParamUUID()
	 */
	@Override
	public String getParamUUID() {
		// TODO Auto-generated method stub
		return _parameterUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getInstructionsParameterReturnTypeField()
	 */
	@Override
	public ReturnType getInstructionsParameterReturnTypeField() {
		// TODO Auto-generated method stub
		return _returnType;
	}
	
	/**
	 * @param _parameterName the _parameterName to set
	 */
	public void setParameter(String parameterName, String parameterValue) {
		this._parameterName = parameterName;
		this._parameterValue = parameterValue;
	}

	/**
	 * @param _parameterUUID the _parameterUUID to set
	 */
	public void setParameterUUID(String parameterUUID) {
		this._parameterUUID = parameterUUID;
	}

	/**
	 * @param _returnType the _returnType to set
	 */
	public void setReturnType(ReturnType returnType) {
		this._returnType = returnType;
	}

}
