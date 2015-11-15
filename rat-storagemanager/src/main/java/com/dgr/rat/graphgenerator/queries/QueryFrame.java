/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.graphgenerator.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IQueryFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATQueryEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATVertexFrame;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class QueryFrame{
	private String _startPipeInstruction = null;
    private String _internalPipeInstruction = null;
    private String _endPipeInstruction = null;
	
	private String _commandVersion = null;
	private JSONType _commandType = null;
	private UUID _commandUUID = null;
	private UUID _rootNodeUUID = null;
	private FramedGraph<Graph> _queryGraph = null;
	private IQueryFrame _rootNode = null;
	private List<IInstructionParameterNodeFrame>_instructionParameters = new ArrayList<IInstructionParameterNodeFrame>();
	
	public QueryFrame(String commandVersion) {
		set_commandVersion(commandVersion);
		_commandType = JSONType.Query;
	}
	
	private IInstructionParameterNodeFrame addInstructionParameter(String paramName, String paramValue, ReturnType returnType, int orderField){
		IInstructionParameterNodeFrame instructionParameter = _queryGraph.addVertex(null, IInstructionParameterNodeFrame.class);
		instructionParameter.setVertexUserCommandsInstructionsParameterNameField(paramName);
		instructionParameter.setVertexUserCommandsInstructionsParameterValueField(paramValue);
		instructionParameter.setVertexUserCommandsInstructionsParameterReturnTypeField(returnType);
		instructionParameter.setVertexLabelField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexContentField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexUUIDField(UUID.randomUUID().toString());
		instructionParameter.setVertexRoleValueRootField(false);
		instructionParameter.setVertexTypeField(VertexType.InstructionParameter);
		instructionParameter.setInstructionOrderField(orderField);
		
		return instructionParameter;
	}
	
	private void addInstructionParameter(String paramName, String paramValue, ReturnType returnType){
		IInstructionParameterNodeFrame instructionParameter = _queryGraph.addVertex(null, IInstructionParameterNodeFrame.class);
		instructionParameter.setVertexUserCommandsInstructionsParameterNameField(paramName);
		instructionParameter.setVertexUserCommandsInstructionsParameterValueField(paramValue);
		instructionParameter.setVertexUserCommandsInstructionsParameterReturnTypeField(returnType);
		instructionParameter.setVertexLabelField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexContentField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexUUIDField(UUID.randomUUID().toString());
		instructionParameter.setVertexRoleValueRootField(false);
		instructionParameter.setVertexTypeField(VertexType.InstructionParameter);
		
		int orderField = _instructionParameters.size() - 1;
		orderField = orderField < 0 ? 1 : orderField;
		instructionParameter.setInstructionOrderField(orderField);
		
		_instructionParameters.add(instructionParameter);
	}
	
	private IInstructionNodeFrame addInstruction(IInstructionParameterNodeFrame instructionParameter, String commandName){
		IInstructionNodeFrame instruction = _queryGraph.addVertex(null, IInstructionNodeFrame.class);
		instruction.setInstructionOrderField(1);
		instruction.setVertexContentField(commandName);
		instruction.setVertexLabelField(commandName);
		instruction.setVertexRoleValueRootField(false);
		instruction.setVertexTypeField(VertexType.Instruction);
		UUID uuid = UUID.randomUUID();
		instruction.setVertexUUIDField(uuid.toString());
		instruction.setMaxNumParameters(1);
		
		Edge edge = instruction.asVertex().addEdge(RATConstants.EdgeInstructionParameter, instructionParameter.asVertex());
		IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
		framedEdge.setWeight(0);
		
		return instruction;
	}
	
	private IInstructionNodeFrame addInstruction(List<IInstructionParameterNodeFrame>instructionParameters, String commandName){
		IInstructionNodeFrame instruction = _queryGraph.addVertex(null, IInstructionNodeFrame.class);
		instruction.setInstructionOrderField(1);
		instruction.setVertexContentField(commandName);
		instruction.setVertexLabelField(commandName);
		instruction.setVertexRoleValueRootField(false);
		instruction.setVertexTypeField(VertexType.Instruction);
		UUID uuid = UUID.randomUUID();
		instruction.setVertexUUIDField(uuid.toString());
		instruction.setMaxNumParameters(instructionParameters.size());
		
		int inc = 0;
		for(IInstructionParameterNodeFrame instructionParameter : instructionParameters){
			Edge edge = instruction.asVertex().addEdge(RATConstants.EdgeInstructionParameter, instructionParameter.asVertex());
			IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
			framedEdge.setWeight(inc);
			inc++;
		}
		
		return instruction;
	}
	
	private void addParameter(IInstructionNodeFrame instruction, IInstructionParameterNodeFrame instructionParameter, int maxNum, int weight){
		instruction.setMaxNumParameters(maxNum);
		Edge edge = instruction.asVertex().addEdge(RATConstants.EdgeInstructionParameter, instructionParameter.asVertex());
		IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
		framedEdge.setWeight(weight);
	}
	
	public void addNodesToGraph(List<Vertex> list, Vertex commandVertex, String param, ReturnType type, String queryName) throws Exception {
		// COMMENT: per risolvere il problema di BindToGraph che non ha root vertex finali come tutti gli altri
		// TODO: questo problema è da risolvere usando la proprietà del TowardNode del QueryPivot
		int size = list.size();
		Stack<IRATVertexFrame>stack = new Stack<IRATVertexFrame>();
		Graph queryGraph = new TinkerGraph();
		_queryGraph = new FramedGraphFactory(new JavaHandlerModule()).create(queryGraph);
		String commandName = null;
		
		// COMMENT: per risolvere il problema di BindToGraph che non ha root vertex finali come tutti gli altri
		// questo problema è da risolvere usando la proprietà del TowardNode del QueryPivot
		Vertex tmp = list.get(0);
		VertexType vertexType = VertexType.fromString(tmp.getProperty(RATConstants.VertexTypeField).toString());
		if(vertexType.toString().equalsIgnoreCase(VertexType.SystemKey.toString())){
			// COMMENT: l'ultimo nodo è un SystemKey: stiamo creando BindToGraph
			// COMMENT: prendo il primo nodo che sicuramente è un QueryPivot
			Vertex queryPivot = list.get(size - 1);
			// COMMENT: prendo la proprietà FromNode del QueryPivot
			VertexType fromNode = VertexType.fromString(queryPivot.getProperty(RATConstants.TowardNode).toString());
			// COMMENT: creo un nuovo graph perché se usassi queryGraph finirei con l'avere due nuovi nodi nel grafo.
			Graph tmpGraph = new TinkerGraph();
			// COMMENT: creo un nuovo vertex col tipo recuperato dal queryPivot e gli imposto il VertexType
			Vertex newVertex = tmpGraph.addVertex(null);
			newVertex.setProperty(RATConstants.VertexTypeField, fromNode);
			newVertex.setProperty(RATConstants.VertexCommandOwnerField, queryPivot.getProperty(RATConstants.VertexCommandOwnerField));
			newVertex.setProperty(RATConstants.VertexContentField, fromNode.toString());
			newVertex.setProperty(RATConstants.VertexLabelField, fromNode.toString());
			
			list.add(0, newVertex);
			size = list.size();
		}
		_instructionParameters.clear();
		ListIterator<Vertex> lIt = list.listIterator(list.size());
		while(lIt.hasPrevious()) {
			Vertex vertex = lIt.previous();
//			String edgeLabel = commandVertex.getProperty(RATConstants.VertexCommandOwnerField);
			String edgeLabel = commandVertex.getProperty(RATConstants.VertexCommandOwnerField);
			String name = vertex.getProperty(RATConstants.QueryName);
			if(name != null){
				commandName = name;
			}
			
			vertexType = VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString());
			switch(vertexType){
			case QueryPivot:{
				_startPipeInstruction = vertex.getProperty(RATConstants.StartPipeInstruction);
				_internalPipeInstruction = vertex.getProperty(RATConstants.InternalPipeInstruction);
				_endPipeInstruction = vertex.getProperty(RATConstants.EndPipeInstruction);
				
		        String commandUUIDstr = GraphGeneratorHelpers.getUUID(_endPipeInstruction);
		        _commandUUID = UUID.fromString(commandUUIDstr);

//		        IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid, 0);
		        this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		        if(startPipeParam != null && type != null && queryName != null && queryName.equalsIgnoreCase(_endPipeInstruction)){
//		        	this.addInstructionParameter(startPipeParam, RATConstants.VertexContentUndefined, type);
//		        }
		        
				UUID uuid = UUID.randomUUID();
				
				IInstructionNodeFrame instruction = this.addInstruction(_instructionParameters, commandName);
//				IInstructionNodeFrame instruction = this.addInstruction(instructionParameter, commandName);
				
				_rootNode = _queryGraph.addVertex(null, IQueryFrame.class);
				_rootNode.setVertexCommandOwnerField(commandName);
				_rootNode.setVertexContentField(VertexType.QueryPivot.toString());
				_rootNode.setVertexLabelField(VertexType.QueryPivot.toString());
				_rootNode.setIsRootVertexField(true);
				_rootNode.setVertexTypeField(VertexType.QueryPivot);
				_rootNode.setCommandGraphUUID(_commandUUID.toString());
				_rootNode.setTowardNode(VertexType.fromString(vertex.getProperty(RATConstants.TowardNode).toString()));
				_rootNode.setFromNode(VertexType.fromString(vertex.getProperty(RATConstants.FromNode).toString()));
				uuid = UUID.randomUUID();
				_rootNode.setVertexUUIDField(uuid.toString());

				Edge edge = _rootNode.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
				IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
				framedEdge.setWeight(0);

		        _rootNodeUUID = uuid;
		        
				stack.push(_rootNode);
			}
				break;
				
			case SystemKey:{
				IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter(RATConstants.VertexContentField, 
						vertex.getProperty(RATConstants.VertexContentField).toString(), ReturnType.string, 0);
				IInstructionNodeFrame instruction = this.addInstruction(instructionParameter, _internalPipeInstruction);
				
				instructionParameter = this.addInstructionParameter("edgeLabel", edgeLabel, ReturnType.string, 1);
				this.addParameter(instruction, instructionParameter, 2, 1);
				
				IRATVertexFrame node = _queryGraph.addVertex(null, IQueryFrame.class);
				node.setVertexCommandOwnerField(vertex.getProperty(RATConstants.VertexCommandOwnerField).toString());
				node.setVertexContentField(vertex.getProperty(RATConstants.VertexContentField).toString());
				node.setVertexLabelField(vertex.getProperty(RATConstants.VertexLabelField).toString());
				node.setIsRootVertexField(false);
				node.setVertexTypeField(VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString()));
				node.setCommandGraphUUID(_commandUUID.toString());
				UUID uuid = UUID.randomUUID();
				node.setVertexUUIDField(uuid.toString());
				
				IRATVertexFrame prev = stack.pop();
				// TODO: da spostare dentro la classe Impl in IRATVertexFrame utilizzando @JavaHandler
				Edge edge = prev.asVertex().addEdge(edgeLabel, node.asVertex());
				IRATQueryEdgeFrame framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
				framedEdge.setWeight(0);
				framedEdge.setCommandGraphUUID(_commandUUID.toString());
				
				edge = node.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
				framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
				framedEdge.setWeight(0);
				
				stack.push(node);
				
			}
				break;
				
			default:{
				_instructionParameters.clear();
				if(param != null && type != null && queryName != null && queryName.equalsIgnoreCase(_endPipeInstruction)){
					this.buildFinalNode(vertex, edgeLabel, stack, _endPipeInstruction, param, type);
				}
				else{
					this.buildFinalNode(vertex, edgeLabel, stack, _endPipeInstruction, null, null);
				}
			}
				break;
					
			}
		}
	}
    
	private void buildFinalNode(Vertex vertex, String edgeLabel, Stack<IRATVertexFrame>stack, String queryName, String param, ReturnType type){
	    
//		IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter(RATConstants.VertexTypeField, 
//				vertex.getProperty(RATConstants.VertexTypeField).toString(), ReturnType.string, 0);
//		IInstructionNodeFrame instruction = this.addInstruction(instructionParameter, queryName);
		
//		instructionParameter = this.addInstructionParameter("edgeLabel", edgeLabel, ReturnType.string, 1);
//		this.addParameter(instruction, instructionParameter, 2, 1);
		
		this.addInstructionParameter(RATConstants.VertexTypeField, vertex.getProperty(RATConstants.VertexTypeField).toString(), ReturnType.string);
		this.addInstructionParameter("edgeLabel", edgeLabel, ReturnType.string);
	    if(param != null && type != null){
	    	this.addInstructionParameter(param, RATConstants.VertexContentUndefined, type);
	    }
	    IInstructionNodeFrame instruction = this.addInstruction(_instructionParameters, queryName);
		
		IRATVertexFrame node = _queryGraph.addVertex(null, IQueryFrame.class);
		node.setVertexCommandOwnerField(vertex.getProperty(RATConstants.VertexCommandOwnerField).toString());
		node.setVertexContentField(vertex.getProperty(RATConstants.VertexContentField).toString());
		node.setVertexLabelField(vertex.getProperty(RATConstants.VertexLabelField).toString());
		node.setIsRootVertexField(false);
		node.setVertexTypeField(VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString()));
		node.setCommandGraphUUID(_commandUUID.toString());
		UUID uuid = UUID.randomUUID();
		node.setVertexUUIDField(uuid.toString());
		
		IRATVertexFrame prev = stack.pop();
		// TODO: da spostare dentro la classe Impl in IRATVertexFrame utilizzando @JavaHandler
		Edge edge = prev.asVertex().addEdge(edgeLabel, node.asVertex());
		IRATQueryEdgeFrame framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
		framedEdge.setWeight(0);
		framedEdge.setCommandGraphUUID(_commandUUID.toString());
		
		edge = node.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
		framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
		framedEdge.setWeight(0);
	}
	
//	private void buildFinalNode(Vertex vertex, String edgeLabel, Stack<IRATVertexFrame>stack, String queryName){
//		IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter(RATConstants.VertexTypeField, 
//				vertex.getProperty(RATConstants.VertexTypeField).toString(), ReturnType.string, 0);
//		IInstructionNodeFrame instruction = this.addInstruction(instructionParameter, queryName);
//		
//		instructionParameter = this.addInstructionParameter("edgeLabel", edgeLabel, ReturnType.string, 1);
//		this.addParameter(instruction, instructionParameter, 2, 1);
//		
//		IRATVertexFrame node = _queryGraph.addVertex(null, IQueryFrame.class);
//		node.setVertexCommandOwnerField(vertex.getProperty(RATConstants.VertexCommandOwnerField).toString());
//		node.setVertexContentField(vertex.getProperty(RATConstants.VertexContentField).toString());
//		node.setVertexLabelField(vertex.getProperty(RATConstants.VertexLabelField).toString());
//		node.setIsRootVertexField(false);
//		node.setVertexTypeField(VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString()));
//		node.setCommandGraphUUID(_commandUUID.toString());
//		UUID uuid = UUID.randomUUID();
//		node.setVertexUUIDField(uuid.toString());
//		
//		IRATVertexFrame prev = stack.pop();
//		// TODO: da spostare dentro la classe Impl in IRATVertexFrame utilizzando @JavaHandler
//		Edge edge = prev.asVertex().addEdge(edgeLabel, node.asVertex());
//		IRATQueryEdgeFrame framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
//		framedEdge.setWeight(0);
//		framedEdge.setCommandGraphUUID(_commandUUID.toString());
//		
//		edge = node.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
//		framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
//		framedEdge.setWeight(0);
//	}
	
	/**
	 * @return the _commandVersion
	 */
	public String get_commandVersion() {
		return _commandVersion;
	}

	/**
	 * @param _commandVersion the _commandVersion to set
	 */
	private void set_commandVersion(String _commandVersion) {
		this._commandVersion = _commandVersion;
	}
	
	/**
	 * @return the _commandType
	 */
	public JSONType get_commandType() {
		return _commandType;
	}
	
	public UUID get_commandUUID() {
		return _commandUUID;
	}
	
	/**
	 * @return the _rootNodeUUID
	 */
	public UUID get_rootNodeUUID() {
		return _rootNodeUUID;
	}

	public Graph getGraph() {
		return _queryGraph;
	}

	public Vertex get_rootNode() {
		return _rootNode.asVertex();
	}

	/**
	 * @return the _startPipeInstruction
	 */
	public String getStartPipeInstruction() {
		return _startPipeInstruction;
	}

	/**
	 * @param _startPipeInstruction the _startPipeInstruction to set
	 */
	public void setStartPipeInstruction(String startPipeInstruction) {
		this._startPipeInstruction = startPipeInstruction;
	}

	/**
	 * @return the _internalPipeInstruction
	 */
	public String geInternalPipeInstruction() {
		return _internalPipeInstruction;
	}

	/**
	 * @param _internalPipeInstruction the _internalPipeInstruction to set
	 */
	public void setInternalPipeInstruction(String internalPipeInstruction) {
		this._internalPipeInstruction = internalPipeInstruction;
	}

	/**
	 * @return the _endPipeInstruction
	 */
	public String getEndPipeInstruction() {
		return _endPipeInstruction;
	}

	/**
	 * @param _endPipeInstruction the _endPipeInstruction to set
	 */
	public void setEndPipeInstruction(String endPipeInstruction) {
		this._endPipeInstruction = endPipeInstruction;
	}
}
