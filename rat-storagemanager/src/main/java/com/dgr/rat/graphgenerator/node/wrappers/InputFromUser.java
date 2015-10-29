/**
 * @author Daniele Grignani (dgr)
 * @date Oct 14, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class InputFromUser extends CommandNode{

	public InputFromUser(AbstractCommand command, boolean isRootNode) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.UserContent);
		this.set_nodeContent(VertexType.UserContent.toString());
		this.set_label(VertexType.UserContent.toString());
		this.set_commandOwner(command.get_commandName());
	}

}
