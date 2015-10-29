/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class UserName extends CommandNode{

	public UserName(AbstractCommand command, boolean isRootNode) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.UserName);
		this.set_nodeContent(VertexType.UserName.toString());
		this.set_label(VertexType.UserName.toString());
		this.set_commandOwner(command.get_commandName());
	}
}
