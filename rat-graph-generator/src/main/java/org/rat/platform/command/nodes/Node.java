package org.rat.platform.command.nodes;

import com.dgr.rat.json.utils.VertexType;

public class Node extends CommandNode{

	public Node(VertexType type, String label, String content) {
		super.setType(type);
		super.setLabel(label);
		super.setContent(content);
	}

}
