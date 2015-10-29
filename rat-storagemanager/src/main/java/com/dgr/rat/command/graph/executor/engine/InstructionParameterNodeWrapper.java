/**
 * @author Daniele Grignani (dgr)
 * @date Sep 13, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.json.utils.ReturnType;

public class InstructionParameterNodeWrapper implements IInstructionParam{
	private IInstructionParameterNodeFrame _instructionParameter = null;
	
	public InstructionParameterNodeWrapper(IInstructionParameterNodeFrame instructionParameter) {
		_instructionParameter = instructionParameter;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getVertexUserCommandsInstructionsParameterNameField()
	 */
	@Override
	public String getInstructionsParameterNameField() {
		// TODO Auto-generated method stub
		return _instructionParameter.getVertexUserCommandsInstructionsParameterNameField();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getVertexUserCommandsInstructionsParameterValueField()
	 */
	@Override
	public String getInstructionsParameterValueField() {
		// TODO Auto-generated method stub
		return _instructionParameter.getVertexUserCommandsInstructionsParameterValueField();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getVertexUserCommandsInstructionsParameterReturnTypeField()
	 */
	@Override
	public ReturnType getInstructionsParameterReturnTypeField() {
		// TODO Auto-generated method stub
		return _instructionParameter.getVertexUserCommandsInstructionsParameterReturnTypeField();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInstructionParam#getParamUUID()
	 */
	@Override
	public String getParamUUID() {
		// TODO Auto-generated method stub
		return _instructionParameter.getVertexUUIDField();
	}
}
