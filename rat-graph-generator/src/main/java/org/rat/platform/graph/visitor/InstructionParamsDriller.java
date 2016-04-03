package org.rat.platform.graph.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rat.platform.command.nodes.CommandNode;
import org.rat.platform.command.nodes.InstructionParameter;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstructionParamsDriller implements IVisitor{
	private List<CommandNode>_visited = new ArrayList<CommandNode>();
	private Map<String, Map<String, Object>>_values = new HashMap<String, Map<String, Object>>();
	
	public InstructionParamsDriller() {
		// TODO Auto-generated constructor stub
	}
	
	public String getValues() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(_values);
		
		return output;
	}
	
	public void addNodeAlreadyVisited(CommandNode node){
		_visited.add(node);
	}
	
	public boolean alreadyVisited(CommandNode node){
		return _visited.contains(node);
	}

	@Override
	public void visit(CommandNode node) {
		//System.out.println("Node " + node.getContent());
		if(this.alreadyVisited(node)){
			return;
		}
		this.addNodeAlreadyVisited(node);
		
		if(node.getType() == VertexType.InstructionParameter){
			InstructionParameter param = (InstructionParameter)node;
			if(!param.isUndefined()){
				return;
			}
			
			//String name = node.getContent();
			String parentName = node.getParent().getContent();
			Object returnType = param.getReturnType();//node.getProperty(RATConstants.VertexInstructionParameterReturnTypeField);
			String paramName = param.getParamName();//node.getProperty(RATConstants.VertexInstructionParameterNameField);
			String paramValue = param.getParamValue();//node.getProperty(RATConstants.VertexInstructionParameterValueField);
			Object orderedField = node.getCommandNodeProperty(RATConstants.InstructionOrderField);
			
			Map<String, Object>values = new HashMap<String, Object>();
			//values.put(RATConstants.VertexInstructionParameterNameField, name);
			values.put(RATConstants.VertexInstructionOwnerNameField, parentName);
			values.put(RATConstants.VertexInstructionParameterReturnTypeField, returnType);
			//values.put("ReturnType", returnType);
			values.put(RATConstants.VertexInstructionParameterNameField, paramName);
			values.put(RATConstants.VertexInstructionParameterValueField, paramValue);
			values.put(RATConstants.InstructionOrderField, orderedField);
			
			_values.put(paramName, values);
		}
		else{
			Iterator<String> it = node.getInstructionsIt();
			while(it.hasNext()){
				String key = it.next();
				CommandNode child = node.getInstruction(key);
				//System.out.println("Instruction " + child.getContent());
				child.accept(this);
			}
			
			Iterator<Integer> it2 = node.getChildrenIt();
			while(it2.hasNext()){
				int key = it2.next();
				CommandNode child = node.getChild(key);
				//System.out.println("child " + child.getContent());
				child.accept(this);
			}
		}
	}
}
