/**
 * @author Daniele Grignani (dgr)
 * @date Sep 28, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import com.dgr.rat.json.utils.ReturnType;

public interface IInstructionParam {
    public String getInstructionsParameterNameField();
    public String getInstructionsParameterValueField();
    public String getParamUUID();
    public ReturnType getInstructionsParameterReturnTypeField();
}
