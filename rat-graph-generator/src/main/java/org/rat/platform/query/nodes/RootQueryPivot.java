package org.rat.platform.query.nodes;

import org.rat.platform.command.nodes.CommandNode;
import org.rat.platform.graph.visitor.IVisitor;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class RootQueryPivot extends CommandNode{
	
	public RootQueryPivot(String label, String content) {
		super.setIsRoot(true);
		super.setType(VertexType.QueryPivot);
		super.setLabel(label);
		super.setContent(content);
	}
	
//	public void addInstruction(String nodePath, String instructionName, String paramName, String paramValue, ReturnType returnType) throws Exception{
//		rootNode.addInstruction("StartQueryPipe", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.string);
//	}
	
//	public void addSystemKey(SystemKey systemKey){
//		super.addChild(systemKey);
//	}
//	
//	public void addInstruction(Instruction instruction){
//		super.addChild(instruction);
//	}
//	
//	@Override
//	public void accept(IVisitor visitor) {
//		visitor.visit(this);
//		
//		super.accept(visitor);
//	}
}
