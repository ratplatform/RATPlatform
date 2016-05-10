package org.rat.platform.command.nodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.rat.platform.graph.CommandGraph;
import org.rat.platform.graph.visitor.IAcceptor;
import org.rat.platform.graph.visitor.IVisitor;
import org.rat.platform.rat_graph_generator.GraphGeneratorHelpers;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public abstract class CommandNode implements IAcceptor{
	private Map<Integer, CommandNode> _children = new LinkedHashMap<Integer, CommandNode>();
	private Map<String, Instruction> _instructions = new LinkedHashMap<String, Instruction>();
	private Map<String, Object>_properties = new HashMap<String, Object>();
	private CommandNode _parent = null;
	
	public CommandNode() {
		this.addCommandNodeProperty(RATConstants.GraphUUID, false);
		//this.addCommandNodeProperty(RATConstants.IsDeleted, false);
		this.setIsRoot(false);
	}
	
	public CommandNode getParent(){
		return _parent;
	}
	
	public void setParent(CommandNode parent){
		_parent = parent;
	}
	
	public void addChild(CommandNode child){
		child.setParent(this);
		int size = _children.size();
		_children.put(size, child);
	}
	
	public Iterator<String>getCommandNodePropertiesIt(){
		return _properties.keySet().iterator();
	}
	
	public Iterator<String>getInstructionsIt(){
		return _instructions.keySet().iterator();
	}
	
	private void addInstructionToNode(CommandGraph commandGraph, String parentUUIDs){
		Iterator<String> it = this.getInstructionsIt();
		int inc = 0;
		while(it.hasNext()){
			String key = it.next();
			CommandNode child = this.getInstruction(key);
			int numParams = child.getChildrenSize();
			child.addCommandNodeProperty(RATConstants.InstructionOrderField, inc);
			child.addCommandNodeProperty(RATConstants.MaxNumParameters, numParams);
			child.run(commandGraph, parentUUIDs + "-" + key);
			commandGraph.addEdge(this, child, RATConstants.EdgeInstruction);
			inc++;
		}
	}

	public void run(CommandGraph commandGraph, String parentUUIDs){
		if(commandGraph.alreadyVisited(this)){
			return;
		}
		
		commandGraph.addNodeAlreadyVisited(this);
		
		String commandGraphUUID = commandGraph.getUuid();
		_properties.put(RATConstants.CommandGraphUUID, commandGraphUUID);
		_properties.put(RATConstants.GraphCommandOwner, commandGraph.getCommandName());
		
		String query = parentUUIDs + "-" + this.getContent();
		String uuid = GraphGeneratorHelpers.getUUID(query);
		this.setUuid(uuid);
		GraphGeneratorHelpers.store(uuid, this.getContent());
		query = query + "-" + uuid;
		//System.out.println("this " + this.getContent());
		commandGraph.addNodeToGraph(this);
		this.addInstructionToNode(commandGraph, parentUUIDs);
		
		Iterator<Integer> it = this.getChildrenIt();
		while(it.hasNext()){
			int key = it.next();
			CommandNode child = this.getChild(key);
			//System.out.println("child " + child.getContent());
			child.run(commandGraph, query + "-" + key);

			String edgeLabel = (child.getType().toString().equals(VertexType.InstructionParameter.toString())) ? 
					RATConstants.EdgeInstructionParameter.toString() : "Command";
			commandGraph.addEdge(this, child, edgeLabel);
		}
	}
	
	public CommandNode getChild(int key){
		return _children.get(key);
	}
	
	public CommandNode getInstruction(String key){
		return _instructions.get(key);
	}
	
	protected int getChildrenSize(){
		return _children.size();
	}
	
	public Iterator<Integer>getChildrenIt(){
		return _children.keySet().iterator();
	}
	
	public int getInstructionSize(){
		return _instructions.size();
	}
	
	public void addCreateRootPlatformVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		this.addInstruction("CreateRootPlatformVertex", paramName, paramValue, returnType);
	}
	
	public void addPropertyVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		this.addInstruction("AddProperties", paramName, paramValue, returnType);
	}
	
	public void addCreateCommandRootVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		this.addInstruction("CreateRootVertex", paramName, paramValue, returnType);
	}
	
	public void addBindInstruction(final String paramName, final String paramValue) throws Exception {
		this.addInstruction("Bind", paramName, paramValue, ReturnType.uuid);
	}
	
	public void addCreateVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		this.addInstruction("CreateVertex", paramName, paramValue, returnType);
	}
	
	public void addInstruction(final String instructionName) throws Exception{
		this.addInstruction(instructionName, "dummy", "dummy", ReturnType.string);
	}
	
//	public void addInstruction(final String instructionName) throws Exception{
//		this.addInstruction(instructionName, null, null, ReturnType.none);
//	}
	
	public void addInstruction(final String instructionName, final String paramName) throws Exception{
		this.addInstruction(instructionName, paramName, "dummy", ReturnType.none);
	}
	
	public void addInstruction(final String instructionName, final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		Instruction instruction = null;
		
		if(_instructions.containsKey(instructionName)){
			instruction = _instructions.get(instructionName);
		}
		else{
	    	instruction = new Instruction();
	    	instruction.setLabel(instructionName);
	    	instruction.setContent(instructionName);
	    	instruction.setParent(this);
	    	_instructions.put(instructionName, instruction);
		}
		
		if(paramName != null){
			if(!instruction.paramExist(paramName)){
		    	InstructionParameter param = new InstructionParameter(paramName, paramValue, returnType);
				instruction.addParameter(param);
			}
			else{
				throw new Exception("A parameter with name " + paramName + " already exists!");
			}
		}
	}
	
	public void addCommandNodeProperty(String key, Object value){
		_properties.put(key, value);
	}
	
	public Object getCommandNodeProperty(String key){
		return _properties.get(key);
	}

	public String getUuid() {
		return this.getCommandNodeProperty(RATConstants.VertexUUIDField).toString();
	}

	public void setUuid(String uuid) {
		this.addCommandNodeProperty(RATConstants.VertexUUIDField, uuid);
	}

	public VertexType getType() {
		String type = this.getCommandNodeProperty(RATConstants.VertexTypeField).toString();
		return VertexType.fromString(type);
	}

	protected void setType(VertexType type) {
		this.addCommandNodeProperty(RATConstants.VertexTypeField, type.toString());
	}
	
//	public boolean isRoot(){
//		String value = this.getIsRoot();
//		return Boolean.valueOf(value);
//	}

//	public boolean getIsRoot() {
//		String isRoot = this.getProperty(RATConstants.VertexIsRootField);
//		return isRoot;
//	}
	
	public boolean isRoot(){
		return (boolean) this.getCommandNodeProperty(RATConstants.VertexIsRootField);
	}

	protected void setIsRoot(boolean isRoot) {
		this.addCommandNodeProperty(RATConstants.VertexIsRootField, isRoot);
	}

	public String getContent() {
		return this.getCommandNodeProperty(RATConstants.VertexContentField).toString();
	}

	public void setContent(String content) {
		this.addCommandNodeProperty(RATConstants.VertexContentField, content);
	}

	public String getLabel() {
		return this.getCommandNodeProperty(RATConstants.VertexLabelField).toString();
	}

	public void setLabel(String label) {
		this.addCommandNodeProperty(RATConstants.VertexLabelField, label);
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}
