/**
 * @author Daniele Grignani (dgr)
 * @date Sep 27, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.Iterator;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.storage.provider.IStorage;

public interface IInstructionInvoker {
	public void invoke(IInstructionNodeWrapper invokable) throws Exception;
	public Iterator<String>getParameterNameIterator();
	public IStorage getStorage();
	public String getNodeParamValue(String paramName);
	public String getCommandParamValue(String paramName); 
	public InstructionResultContainer getInstructionResult(UUID nodeUUID);
	public CommandResponse getCommandResponse();
	public void addCommandResponse(ICommandNodeVisitable visited, IInstructionResult instructionResult) throws Exception;
	public String getValueByIndex(int index) throws Exception;
	public int getNumOfParameters();
	public String getCurrentInstruction();
	public UUID getGraphUUID();
	public void setGraphUUID(UUID uuid);
}
