/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.tinkerpop.frames.Property;

public interface IInstructionParameterNodeFrame extends IRATInstructionFrame{
    @Property(RATConstants.VertexInstructionParameterNameField)
    public void setVertexUserCommandsInstructionsParameterNameField(final String parameterName);
    
    @Property(RATConstants.VertexInstructionParameterNameField)
    public String getVertexUserCommandsInstructionsParameterNameField();
    
    @Property(RATConstants.VertexInstructionParameterValueField)
    public void setVertexUserCommandsInstructionsParameterValueField(final String parameterValue);
    
    @Property(RATConstants.VertexInstructionParameterValueField)
    public String getVertexUserCommandsInstructionsParameterValueField();
    
    @Property(RATConstants.VertexInstructionParameterReturnTypeField)
    public void setVertexUserCommandsInstructionsParameterReturnTypeField(final ReturnType returnType);
    
    @Property(RATConstants.VertexInstructionParameterReturnTypeField)
    public ReturnType getVertexUserCommandsInstructionsParameterReturnTypeField();
}
