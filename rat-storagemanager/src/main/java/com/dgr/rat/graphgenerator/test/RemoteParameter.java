/**
 * @author Daniele Grignani (dgr)
 * @date Oct 12, 2015
 */

package com.dgr.rat.graphgenerator.test;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteParameter {
	private String _uuid = null;
	private Integer _instructionOrder = 0;
	private ReturnType _returnType = ReturnType.unknown;
	private String _parameterName = null;
	private String _parameterValue = null;
	
	public RemoteParameter() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonProperty(RATConstants.VertexUUIDField)
    public void setVertexUUIDField(final String uuid){
		_uuid = uuid;
	}
    public String getVertexUUIDField(){
    	return _uuid;
    }
    
	@JsonProperty(RATConstants.InstructionOrderField)
	public void setInstructionOrder(final Integer instructionOrder){
		_instructionOrder = instructionOrder;
	}
    public Integer getInstructionOrder(){
    	return _instructionOrder;
    }

	/**
	 * @return the _parameterName
	 */
	public String getParameterName() {
		return _parameterName;
	}

	/**
	 * @param _parameterName the _parameterName to set
	 */
	@JsonProperty(RATConstants.VertexInstructionParameterNameField)
	public void setParameterName(String parameterName) {
		this._parameterName = parameterName;
	}

	/**
	 * @return the _parameterValue
	 */
	public String getParameterValue() {
		return _parameterValue;
	}

	/**
	 * @param _parameterValue the _parameterValue to set
	 */
	@JsonProperty(RATConstants.VertexInstructionParameterValueField)
	public void setParameterValue(String parameterValue) {
		this._parameterValue = parameterValue;
	}

	/**
	 * @return the _returnType
	 */
	@JsonProperty("ReturnType")
	public ReturnType getReturnType() {
		return _returnType;
	}

	/**
	 * @param _returnType the _returnType to set
	 */
	public void setReturnType(ReturnType returnType) {
		this._returnType = returnType;
	}

}
