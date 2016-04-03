package org.rat.platform.command.nodes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class InstructionParameter extends CommandNode{
	private boolean _isUndefined = false;
	
	public InstructionParameter(String paramName, String paramValue, ReturnType returnType) {
		this.setType(VertexType.InstructionParameter);
		
		this.setParamName(paramName);
		this.setParamValue(paramValue);
		this.setReturnType(returnType);
		
		this.setLabel(paramName);
		this.setContent(VertexType.InstructionParameter.toString());
	}
	
	public boolean isUndefined(){
		return _isUndefined;
	}

	public String getParamName() {
		return this.getCommandNodeProperty(RATConstants.VertexInstructionParameterNameField).toString();
	}

	private void setParamName(String paramName) {
		this.addCommandNodeProperty(RATConstants.VertexInstructionParameterNameField, paramName);
	}

	public String getParamValue() {
		return this.getCommandNodeProperty(RATConstants.VertexInstructionParameterValueField).toString();
	}

	private void setParamValue(String paramValue) {
		if(paramValue.equalsIgnoreCase(RATConstants.VertexContentUndefined)){
			_isUndefined = true;
		}
		this.addCommandNodeProperty(RATConstants.VertexInstructionParameterValueField, paramValue);
	}

	public ReturnType getReturnType() {
		return ReturnType.fromString(this.getCommandNodeProperty(RATConstants.VertexInstructionParameterReturnTypeField).toString());
	}

	private void setReturnType(ReturnType returnType) {
		this.addCommandNodeProperty(RATConstants.VertexInstructionParameterReturnTypeField, returnType.toString());
	}
}
