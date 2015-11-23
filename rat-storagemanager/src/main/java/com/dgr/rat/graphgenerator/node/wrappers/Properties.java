package com.dgr.rat.graphgenerator.node.wrappers;

import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.json.utils.VertexType;

public class Properties extends CommandNode{

	public Properties(AbstractCommand command) {
		this.setRootNode(false);
		this.setType(VertexType.Properties);
		this.set_nodeContent(VertexType.Properties.toString());
		this.set_label(VertexType.Properties.toString());
		this.set_commandOwner(command.get_commandName());
	}

}
