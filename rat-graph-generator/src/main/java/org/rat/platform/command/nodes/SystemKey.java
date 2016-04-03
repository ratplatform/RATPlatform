package org.rat.platform.command.nodes;

import com.dgr.rat.json.utils.VertexType;

public class SystemKey extends CommandNode{
	public SystemKey(String label, String content) {
		super.setType(VertexType.SystemKey);
		super.setLabel(label);
		super.setContent(content);
	}
}
