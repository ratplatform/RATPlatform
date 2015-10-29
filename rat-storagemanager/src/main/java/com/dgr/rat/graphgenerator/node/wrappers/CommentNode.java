/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class CommentNode extends CommandNode{

	public CommentNode(AbstractCommand command, boolean isRootNode, String content) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.Comment);
		this.set_nodeContent(content);
		this.set_label(content);
		this.set_commandOwner(command.get_commandName());
	}

}
