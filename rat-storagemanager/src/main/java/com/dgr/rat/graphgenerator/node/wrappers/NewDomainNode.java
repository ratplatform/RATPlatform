/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class NewDomainNode extends CommandNode{

	public NewDomainNode(AbstractCommand command, boolean isRootNode, String content) {
		this.setRootNode(isRootNode);
		this.setType(VertexType.Domain);
		this.set_nodeContent(content);
		this.set_label(content);
		this.set_commandOwner(command.get_commandName());
	}
}
