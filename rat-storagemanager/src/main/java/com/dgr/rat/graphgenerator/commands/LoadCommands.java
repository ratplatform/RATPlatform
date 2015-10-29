/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.Commands;
import com.dgr.rat.json.utils.ReturnType;

public class LoadCommands extends AbstractCommand{
	
	public LoadCommands(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}
	
	public void addNodesToGraph() throws Exception{
		Commands rootNode = this.buildRootNode(Commands.class, RATConstants.Commands);//new Commands(this, true);
		rootNode.addCreateCommandRootVertexInstruction("name", RATConstants.Commands, ReturnType.string);
		rootNode.addInstruction("LoadCommandsAction", "folder", RATConstants.VertexContentUndefined, ReturnType.string);
	}
}
