/**
 * @author Daniele Grignani (dgr)
 * @date Oct 20, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.commons.constants.JSONType;

public interface ICommandTemplateInvoker {
	public CommandResponse execute(ICommandGraphVisitableFactory visitableFactory, IInstructionInvoker invoker) throws Exception;

	public String getCommandVersion();
	public void setCommandVersion(String commandVersion);
	public void setCommandUUID(UUID commandUUID);
	public UUID getCommandUUID();
	public String getCommandName();
	public void setCommandName(String commandName);
	public JSONType getCommandType();
	public void setCommandType(JSONType commandType);
}
