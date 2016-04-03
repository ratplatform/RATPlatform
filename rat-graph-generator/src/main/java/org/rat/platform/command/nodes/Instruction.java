package org.rat.platform.command.nodes;

import java.util.Iterator;
import org.rat.platform.graph.CommandGraph;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;

public class Instruction extends CommandNode{
	
	public Instruction() {
		super.setType(VertexType.Instruction);
	}
	
	public void addParameter(InstructionParameter parameter){
		parameter.addCommandNodeProperty(RATConstants.InstructionOrderField, super.getChildrenSize());
		super.addChild(parameter);
	}
	
	public int getMaxNumParameters(){
		return super.getChildrenSize();
	}
	
	public boolean paramExist(final String paramName){
		boolean result = false;
		Iterator<Integer> it = this.getChildrenIt();
		while(it.hasNext()){
			int key = it.next();
			CommandNode child = this.getChild(key);
			InstructionParameter instruction = (InstructionParameter) child;
			String name = instruction.getParamName();
			if(name.equalsIgnoreCase(paramName)){
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public void run(CommandGraph commandGraph, String parentUUIDs){
		super.run(commandGraph, parentUUIDs);
	}
}
