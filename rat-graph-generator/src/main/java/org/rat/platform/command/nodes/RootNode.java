package org.rat.platform.command.nodes;

import java.util.ArrayList;
import java.util.List;

import org.rat.platform.graph.CommandGraph;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class RootNode extends CommandNode{
	private List<String> _list = new ArrayList<String>();
	
	public RootNode(String label, String content, VertexType type) {
		super.setIsRoot(true);
		super.setType(type);
		super.setLabel(label);
		super.setContent(content);
		super.addCommandNodeProperty(RATConstants.IsDeleted, false);
	}
	
	public void addSystemKey(SystemKey systemKey){
		super.addChild(systemKey);
	}
	
	public void addInstruction(Instruction instruction){
		super.addChild(instruction);
	}
	
	public void addPropertyVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType, boolean addToIndex) throws Exception{
		if(addToIndex){
			this.addToIndex(paramName);
			//this.addInstruction("AddToIndex", paramName, paramValue, returnType);
		}
		this.addPropertyVertexInstruction(paramName, paramValue, returnType);
	}
	
	public void addToIndex(String field){
		_list.add(field);
	}
	
	public void run(CommandGraph commandGraph, String parentUUIDs){
		if(!_list.isEmpty()){
			String params = "";
			int inc = 0;
			for(String param : _list){
				if(inc == _list.size() - 1){
					params += param;
				}
				else{
					params += param + ",";
				}
			}
			try {
				this.addInstruction("AddToIndex", "fieldsToIndex", params, ReturnType.string);
			} 
			catch (Exception e) {
				// TODO log e gestione
				e.printStackTrace();
			}
		}
		super.run(commandGraph, parentUUIDs);
	}
}
