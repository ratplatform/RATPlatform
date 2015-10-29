/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.GetCommandParamsNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
// TRASH
public class GetCommandParams extends AbstractCommand{

	public GetCommandParams(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}


	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		GetCommandParamsNode rootNode = this.buildRootNode(GetCommandParamsNode.class, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetCommandParams", "commandName", RATConstants.VertexContentUndefined, ReturnType.string);
	}

}
