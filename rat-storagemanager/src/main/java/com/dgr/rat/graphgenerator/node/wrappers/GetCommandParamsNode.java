/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.ICommandCreator;
import com.dgr.rat.json.utils.VertexType;
// TRASH
public class GetCommandParamsNode extends CommandNode{
	public GetCommandParamsNode(ICommandCreator command, boolean isRootNode, String content) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.SystemKey);
		this.set_nodeContent(content);
		this.set_label(content);
		this.set_commandOwner(command.get_commandName());
	}
}
