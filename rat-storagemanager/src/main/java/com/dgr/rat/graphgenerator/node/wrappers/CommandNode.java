/**
 * @author Daniele Grignani (dgr)
 * @date Oct 17, 2015
 */

package com.dgr.rat.graphgenerator.node.wrappers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeQueryPivotFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.InstructionWrapper;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.json.utils.ReturnType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;

public class CommandNode extends AbstractNode<IRATNodeFrame>{
	private boolean _isRootNode = false;
	private LinkedList<CommandNode>_children = new LinkedList<CommandNode>();
//	private LinkedList<QueryPivotNode>_queryPivotNodes = new LinkedList<QueryPivotNode>();
	// TODO: attenzione: se questo nodo avesse due instruction con lo stesso nome, es Bind, sarebbe un vero problema; da correggere!
	private Map<String, InstructionWrapper>_instructions = new LinkedHashMap<String, InstructionWrapper>();
//	private Map<String, LinkedList<QueryPivotNode>>_queryPivotNodes = new LinkedHashMap<String, LinkedList<QueryPivotNode>>();
//	private IRATNodeVertexFrame _node = null;
	
	public CommandNode() {
		// TODO Auto-generated constructor stub
	}

	public void addChild(CommandNode child){
		_children.add(child);
	}
	
//	public void addQueryPivot(String key, String query, boolean isStartPivot){
//		LinkedList<QueryPivotNode>list = null;
//		if(_queryPivotNodes.containsKey(key)){
//			list = _queryPivotNodes.get(key);
//		}
//		else{
//			list = new LinkedList<QueryPivotNode>();
//			_queryPivotNodes.put(key, list);
//		}
//		
//		QueryPivotNode node = new QueryPivotNode(isStartPivot);
//		node.set_nodeUUID(UUID.randomUUID());
//		node.setQueryName(query);
//		node.set_orderField(list.size());
//		node.set_nodeContent(query);
//		node.set_correlationKey(key);
//		
//		list.add(node);
//	}
	
//	public void addQueryPivot(QueryPivotNode queryPivotNode){
//		_queryPivotNodes.add(queryPivotNode);
//	}
	
	// TODO: attenzione: se questo nodo avesse due instruction con lo stesso nome, es Bind, sarebbe un vero problema; da correggere!
	public InstructionWrapper addInstruction(final String instructionName, final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		InstructionWrapper instructionWrapper = null;
		if(_instructions.containsKey(instructionName)){
			instructionWrapper = _instructions.get(instructionName);
			int maxNumParameters = instructionWrapper.getMaxNumParameter();
			instructionWrapper.setMaxNumParameter(++maxNumParameters);
		}
		else{
			int inc = _instructions.size();
			instructionWrapper = new InstructionWrapper(instructionName, inc);
			instructionWrapper.setMaxNumParameter(1);

			instructionWrapper.set_instructionUUID(UUID.randomUUID());
			_instructions.put(instructionName, instructionWrapper);
		}
		
		instructionWrapper.addParam(paramName, paramValue, returnType);
		
		return instructionWrapper;
	}
	
	public InstructionWrapper addCreateCommandRootVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		return this.addInstruction("CreateRootVertex", paramName, paramValue, returnType);
	}
	
	public InstructionWrapper addCreateRootPlatformVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		return this.addInstruction("CreateRootPlatformVertex", paramName, paramValue, returnType);
	}
	
	public InstructionWrapper addCreateVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		return this.addInstruction("CreateVertex", paramName, paramValue, returnType);
	}
	
	public InstructionWrapper addPropertyVertexInstruction(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		return this.addInstruction("AddProperties", paramName, paramValue, returnType);
	}
	
	public InstructionWrapper addBindInstruction(final String paramName, final String node2BindUUID) throws Exception{
		return this.addInstruction("Bind", paramName, node2BindUUID, ReturnType.uuid);
	}
	
	public InstructionWrapper addBindInstruction(final String node2BindUUID) throws Exception{
		return this.addInstruction("Bind", "nodeUUID", node2BindUUID, ReturnType.uuid);
	}
	
	public void addInstruction(InstructionWrapper instructionWrapper) throws Exception{
		if(!_instructions.containsKey(instructionWrapper.getInstructionName())){
			_instructions.put(instructionWrapper.getInstructionName(), instructionWrapper);
		}
	}
	
//	public IRATNodeVertexFrame getNode(){
//		return _node;
//	}
	
	public void buildNodes(FramedGraph<Graph> framedGraph, String commandName, UUID commandUUID, String edgeName) throws Exception {
		if(!this.isAlreadyCreated()){
			this.setNode(framedGraph.addVertex(null, IRATNodeFrame.class));
			this.getNode().setVertexCommandOwnerField(edgeName);
			this.getNode().setVertexContentField(this.get_nodeContent());
			this.getNode().setVertexLabelField(this.get_label());
			this.getNode().setIsRootVertexField(this.isRootNode());
			this.getNode().setVertexTypeField(this.getType());
			this.getNode().setCommandGraphUUID(commandUUID.toString());
			UUID uuid = this.get_nodeUUID();
			if(uuid == null){
				System.out.println("uuid IS NULL");
				throw new Exception();
			}
			this.getNode().setVertexUUIDField(uuid.toString());
			
			this.buildInstructions(framedGraph, this.getNode());
			this.set_isAlreadyCreated(true);
			
			this.buildChildren(framedGraph, this.getNode(), commandName, commandUUID, edgeName);
//			this.buildQueryPivots(framedGraph, this.getNode(), commandName, commandUUID);
		}
	}
	
	private void buildChildren(FramedGraph<Graph> framedGraph, IRATNodeFrame parent, String commandName, UUID commandUUID, String edgeName) throws Exception{
		int inc = 0;
		for(CommandNode child : _children){
			child.buildNodes(framedGraph, commandName, commandUUID, edgeName);
			
			IRATNodeFrame childNode = child.getNode();
			String edgeUUID = GraphGeneratorHelpers.getUUID(this.get_commandOwner() + "-" + inc);
			// COMMENT: non utilizzo addAdjacentVertex perché l'interfaccia mi costringe ad usare una label fissa (Comment, in questo caso: vedi IRATNodeFrame.addAdjacentVertex)
			// e ciò crea dei problemi nella creazione dinamica delle query: DA NON CANCELLARE!
			IRATNodeEdgeFrame edgeFrame = parent.addAdjacentVertex(childNode);
			edgeFrame.setWeight(inc);
			edgeFrame.setEdgeUUID(edgeUUID);
			edgeFrame.setCommandGraphUUID(commandUUID.toString());
			
			// COMMENT: se utilizzo questo in CreateJsonRemoteRequest.makeRemoteClientCommand ho qualche problema. Infatti 
			// navigo tra gli edge usando rootNode.getAdjacentVertices().iterator() che di fatto restituisce dei IRATNodeEdgeFrame.
			// Però usando l'istruzione sotto, non creo dei IRATNodeEdgeFrame, quindi rootNode.getAdjacentVertices().iterator()
			// non restituisce nulla; pertanto in CreateJsonRemoteRequest.makeRemoteClientCommand dovrei iterare tra gli edge
			// e non tra i IRATNodeEdgeFrame come faccio ora
//			Edge edge = parent.asVertex().addEdge(commandName, childNode.asVertex());
//			edge.setProperty("weight", inc);
//			edge.setProperty(RATConstants.EdgeUUIDField, edgeUUID);
//			edge.setProperty(RATConstants.CommandGraphUUID, commandUUID.toString());
			
			inc++;
		}
	}
	
//	private void buildQueryPivots(FramedGraph<Graph> framedGraph, IRATNodeFrame parent, String commandName, UUID commandUUID) throws Exception{
//		Iterator<String>it = _queryPivotNodes.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			LinkedList<QueryPivotNode>list = _queryPivotNodes.get(key);
//			for(QueryPivotNode node : list){
//				node.buildNodes(framedGraph, commandName, commandUUID, RATConstants.QueryPivotEdgeLabel);
//				IRATNodeQueryPivotFrame childNode = node.getNode();
//				parent.asVertex().addEdge(RATConstants.QueryPivotEdgeLabel, childNode.asVertex());
////				parent.addAdjacentVertex(childNode);
//			}
//		}
//	}
	
	private void buildInstructions(FramedGraph<Graph> framedGraph, IRATNodeFrame node) throws Exception{
		Iterator<String> it = _instructions.keySet().iterator();
		while(it.hasNext()){
			String instructionName = it.next();
			InstructionWrapper instructionWrapper = _instructions.get(instructionName);
			instructionWrapper.buildInstructionGraph(framedGraph);
			
			IInstructionEdgeFrame instructionInfo = node.addUserCommandsInstruction(instructionWrapper.getInstruction());
			instructionInfo.setWeight(instructionWrapper.getOrder());
		}
	}
	
	/**
	 * @return the isRootNode
	 */
	public boolean isRootNode() {
		return _isRootNode;
	}

	/**
	 * @param isRootNode the isRootNode to set
	 */
	public void setRootNode(boolean isRootNode) {
		this._isRootNode = isRootNode;
	}
}
