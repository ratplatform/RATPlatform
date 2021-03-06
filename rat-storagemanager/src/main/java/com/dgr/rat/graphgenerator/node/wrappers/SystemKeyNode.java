/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class SystemKeyNode extends CommandNode{
	public SystemKeyNode(AbstractCommand command, boolean isRootNode, String content) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.SystemKey);
		this.set_nodeContent(content);
		this.set_label(content);
		this.set_commandOwner(command.get_commandName());
	}
}
