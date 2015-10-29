/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class UserPwd extends CommandNode{

	public UserPwd(AbstractCommand command, boolean isRootNode) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.UserPwd);
		this.set_nodeContent(VertexType.UserPwd.toString());
		this.set_label(VertexType.UserPwd.toString());
		this.set_commandOwner(command.get_commandName());
	}
}
