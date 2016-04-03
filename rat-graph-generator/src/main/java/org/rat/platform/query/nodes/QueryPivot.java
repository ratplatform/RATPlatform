package org.rat.platform.query.nodes;

import org.rat.platform.command.nodes.CommandNode;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class QueryPivot extends CommandNode{

	public QueryPivot(String label) {
		super.setIsRoot(false);
		super.setType(VertexType.QueryPivot);
		super.setLabel(label);
		super.setContent(VertexType.QueryPivot.toString());
	}
	
//	public void addInstruction(String nodePath, String instructionName, String paramName, String paramValue, ReturnType returnType) throws Exception{
//		rootNode.addInstruction("StartQueryPipe", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.string);
//	}

}
