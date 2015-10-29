/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.Queries;
import com.dgr.rat.json.utils.ReturnType;

public class LoadQueries extends AbstractCommand{
	
	public LoadQueries(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}
	
	public void addNodesToGraph() throws Exception{
		Queries rootNode = this.buildRootNode(Queries.class, RATConstants.Queries);
		rootNode.addCreateCommandRootVertexInstruction("name", RATConstants.Queries, ReturnType.string);
		rootNode.addInstruction("LoadCommandsAction", "folder", RATConstants.VertexContentUndefined, ReturnType.string);
	}
}
