package com.dgr.rat.graphgenerator.queries;

import java.util.LinkedList;
import java.util.List;
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
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class QueryGenerator {
	protected enum Status{
		Root(),
		Others();
	};
	private FramedGraph<Graph> _queryGraph = null;
	private IQueryFrame _rootNode = null;
	private UUID _commandUUID = null;
	private UUID _rootNodeUUID = null;
	private Status _status = Status.Root;
	private JSONType _commandType = null;
	private String _commandName = null;
	private Stack<IRATVertexFrame>_owners = new Stack<IRATVertexFrame>();
	
	public QueryGenerator() {
		_commandType = JSONType.Query;
	}
	
	public void traverse(Vertex rootVertex) throws Exception{
		Graph queryGraph = new TinkerGraph();
		_queryGraph = new FramedGraphFactory(new JavaHandlerModule()).create(queryGraph);
		
		Stack<Vertex>stack = new Stack<Vertex>();
		LinkedList<Vertex> visited = new LinkedList<Vertex>();
		_status = Status.Root;
		String correlationKey = rootVertex.getProperty(RATConstants.CorrelationKey);
		this.setCommandName(correlationKey);
		String queryUUIDstr = GraphGeneratorHelpers.getUUID(correlationKey);
		_commandUUID = UUID.fromString(queryUUIDstr);
		_owners.clear();
		
		stack.push(rootVertex);

		while(!stack.isEmpty()){
			Vertex vertex = stack.pop();
			//System.out.println(vertex);
			
			switch(_status){
				case Root:
					this.createQueryPivotRootNode(vertex);
					_status = Status.Others;
					break;
					
				case Others:
					Vertex parent = visited.peekLast();
					if(parent == null){
						throw new Exception();
					}
					
					this.createQueryPivotNode(vertex, parent);
					break;
				
				default:
					throw new Exception();
			}
			visited.add(vertex);
			
			GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(vertex);
			if(p.outE(correlationKey).hasNext()){
				Vertex nextVertex = p.inV().next();
				if(!visited.contains(nextVertex) ){
					stack.push(nextVertex);
				}
			}
		}
	}
	
	private Vertex getQueryPivotVertexOwner(Vertex queryPivotVertex) throws Exception{
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(queryPivotVertex);
		List<Vertex> list = (List<Vertex>) p.inE(RATConstants.QueryPivotEdgeLabel).outV().toList();
		if(list.size() != 1){
			throw new Exception();
		}
		
		Vertex result = list.get(0);
		
		return result;
	}
	
	private IRATVertexFrame createNewOwnerNode(Vertex queryPivotVertexOwner){
		UUID uuid = UUID.randomUUID();
		IRATVertexFrame currentQueryPivotNodeOwner = _queryGraph.addVertex(null, IRATVertexFrame.class);
		currentQueryPivotNodeOwner.setVertexCommandOwnerField(queryPivotVertexOwner.getProperty(RATConstants.GraphCommandOwner).toString());
		currentQueryPivotNodeOwner.setVertexContentField(queryPivotVertexOwner.getProperty(RATConstants.VertexContentField).toString());
		currentQueryPivotNodeOwner.setVertexLabelField(queryPivotVertexOwner.getProperty(RATConstants.VertexLabelField).toString());
		currentQueryPivotNodeOwner.setIsRootVertexField(false);
		currentQueryPivotNodeOwner.setVertexTypeField(VertexType.fromString(queryPivotVertexOwner.getProperty(RATConstants.VertexTypeField).toString()));
		currentQueryPivotNodeOwner.setCommandGraphUUID(_commandUUID.toString());
		currentQueryPivotNodeOwner.setVertexUUIDField(uuid.toString());
		
		return currentQueryPivotNodeOwner;
	}
	
	private void createQueryPivotRootNode(Vertex vertex){
		List<IInstructionParameterNodeFrame>instructionParameters = new LinkedList<IInstructionParameterNodeFrame>();
		
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(vertex);
		@SuppressWarnings("unchecked")
		List<Vertex> list = (List<Vertex>) p.outE(RATConstants.QueryPivotEdgeLabel).inV().has(RATConstants.VertexTypeField, VertexType.InstructionParameter.toString()).toList();
		IInstructionParameterNodeFrame instructionParameter = null;
		if(list.size() > 0){
			for(Vertex param : list){
				instructionParameter = this.addInstructionParameter(param.getProperty(RATConstants.VertexInstructionParameterNameField).toString(), 
						param.getProperty(RATConstants.VertexInstructionParameterValueField).toString(), 
						ReturnType.fromString(param.getProperty(RATConstants.VertexInstructionParameterReturnTypeField).toString()));
				instructionParameters.add(instructionParameter);
			}
			//instructionParameter = this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		}
		else{
			instructionParameter = this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
			instructionParameters.add(instructionParameter);
		}
		
//		IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		instructionParameters.add(instructionParameter);
		
		String queryName = vertex.getProperty(RATConstants.QueryName);

		UUID uuid = UUID.randomUUID();
		
		IInstructionNodeFrame instruction = this.addInstruction(instructionParameters, queryName);
		
		_rootNode = _queryGraph.addVertex(null, IQueryFrame.class);
		_rootNode.setVertexCommandOwnerField(queryName);
		_rootNode.setVertexContentField(VertexType.QueryPivot.toString());
		_rootNode.setVertexLabelField(VertexType.QueryPivot.toString());
		_rootNode.setIsRootVertexField(true);
		_rootNode.setVertexTypeField(VertexType.QueryPivot);
		_rootNode.setCommandGraphUUID(_commandUUID.toString());
		_rootNode.setVertexUUIDField(uuid.toString());

		Edge edge = _rootNode.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
		IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
		framedEdge.setWeight(0);

		//System.out.println("Push: " + _rootNode.getVertexLabelField());
		_owners.push(_rootNode);
		
        _rootNodeUUID = uuid;
	}
	
//	private void createQueryPivotRootNode(Vertex vertex){
//		List<IInstructionParameterNodeFrame>instructionParameters = new LinkedList<IInstructionParameterNodeFrame>();
//		IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter("rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		instructionParameters.add(instructionParameter);
//		
//		String queryName = vertex.getProperty(RATConstants.QueryName);
//
//		UUID uuid = UUID.randomUUID();
//		
//		IInstructionNodeFrame instruction = this.addInstruction(instructionParameters, queryName);
//		
//		_rootNode = _queryGraph.addVertex(null, IQueryFrame.class);
//		_rootNode.setVertexCommandOwnerField(queryName);
//		_rootNode.setVertexContentField(VertexType.QueryPivot.toString());
//		_rootNode.setVertexLabelField(VertexType.QueryPivot.toString());
//		_rootNode.setIsRootVertexField(true);
//		_rootNode.setVertexTypeField(VertexType.QueryPivot);
//		_rootNode.setCommandGraphUUID(_commandUUID.toString());
//		_rootNode.setVertexUUIDField(uuid.toString());
//
//		Edge edge = _rootNode.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
//		IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
//		framedEdge.setWeight(0);
//
//		//System.out.println("Push: " + _rootNode.getVertexLabelField());
//		_owners.push(_rootNode);
//		
//        _rootNodeUUID = uuid;
//	}
	
	private void createQueryPivotNode(Vertex currentQueryPivotVertex, Vertex previousQueryPivotVertex) throws Exception{
		String queryName = currentQueryPivotVertex.getProperty(RATConstants.QueryName);
		
		List<IInstructionParameterNodeFrame>instructionParameters = new LinkedList<IInstructionParameterNodeFrame>();

		Vertex currentQueryPivotVertexOwner = this.getQueryPivotVertexOwner(currentQueryPivotVertex);
		//Vertex previousQueryPivotVertexOwner = this.getQueryPivotVertexOwner(previousQueryPivotVertex);
		
		String edgeLabel = currentQueryPivotVertexOwner.getProperty(RATConstants.GraphCommandOwner);
		VertexType vertexType = VertexType.fromString(currentQueryPivotVertexOwner.getProperty(RATConstants.VertexTypeField).toString());
		
		if(vertexType.equals(VertexType.InstructionParameter)){
			// COMMENT: creo il parametro
			String paramName = currentQueryPivotVertexOwner.getProperty(RATConstants.VertexInstructionParameterNameField);
			String paramValue = currentQueryPivotVertexOwner.getProperty(RATConstants.VertexInstructionParameterValueField);
			ReturnType returnType = ReturnType.fromString(currentQueryPivotVertexOwner.getProperty(RATConstants.VertexInstructionParameterReturnTypeField).toString());
			
			IInstructionParameterNodeFrame instructionParameter = this.addInstructionParameter(paramName, paramValue, returnType);
			instructionParameters.add(instructionParameter);
			edgeLabel = RATConstants.EdgeInstructionParameter;
			
			IInstructionNodeFrame instruction = this.addInstruction(instructionParameters, queryName);
			
			// COMMENT: recupero il nodo Properties;
			if(_owners.isEmpty()){
				throw new Exception();
			}
			IRATVertexFrame previousQueryPivotNodeOwner = _owners.pop();
			//System.out.println("Pop: " + previousQueryPivotNodeOwner.getVertexLabelField());
			
			GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(previousQueryPivotNodeOwner.asVertex());
			int num = (int) p.outE(RATConstants.EdgeInstruction).count();
			//num = num > 0 ? num - 1 : 0;
			instruction.setInstructionOrderField((int) num);
			
			Edge edge = previousQueryPivotNodeOwner.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
			IRATQueryEdgeFrame framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
			framedEdge.setWeight(num);
			framedEdge.setCommandGraphUUID(_commandUUID.toString());
		}
		else{
			IInstructionParameterNodeFrame instructionParameter = null;
			String paramValue = null;
			String paramName = null;
			if(vertexType.equals(VertexType.SystemKey)){
				paramValue = currentQueryPivotVertexOwner.getProperty(RATConstants.VertexContentField).toString();
				paramName = RATConstants.VertexContentField;
			}
			else{
				// TODO aggiungere il tipo VertexType in ReturnType
				paramValue = currentQueryPivotVertexOwner.getProperty(RATConstants.VertexTypeField).toString();
				paramName = RATConstants.VertexTypeField;
			}
			instructionParameter = this.addInstructionParameter(paramName, paramValue, ReturnType.string);
			instructionParameters.add(instructionParameter);
			instructionParameter = this.addInstructionParameter("edgeLabel", edgeLabel, ReturnType.string);
			instructionParameters.add(instructionParameter);
			
			GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(currentQueryPivotVertex);
			List<Vertex> list = (List<Vertex>) p.outE(RATConstants.QueryPivotEdgeLabel).inV().toList();//.has(RATConstants.VertexTypeField, VertexType.InstructionParameter).toList();
			if(list.size() > 0){
				for(Vertex param : list){
					instructionParameter = this.addInstructionParameter(param.getProperty(RATConstants.VertexInstructionParameterNameField).toString(), 
							param.getProperty(RATConstants.VertexInstructionParameterValueField).toString(), 
							ReturnType.fromString(param.getProperty(RATConstants.VertexInstructionParameterReturnTypeField).toString()));
					instructionParameters.add(instructionParameter);
				}
			}
			// COMMENT: creo l'instruction
			IInstructionNodeFrame instruction = this.addInstruction(instructionParameters, queryName);
			
			// COMMENT: duplico il vertex al quale è collegata l'attuale QueryPivot
			IRATVertexFrame currentQueryPivotNodeOwner = this.createNewOwnerNode(currentQueryPivotVertexOwner);
			
			// COMMENT: collego l'instruction al duplicato
			int num = (int) currentQueryPivotNodeOwner.getNumberOfInstructions();
			//num = num > 0 ? num - 1 : 0;
			instruction.setInstructionOrderField(num);
			Edge edge = currentQueryPivotNodeOwner.asVertex().addEdge(RATConstants.EdgeInstruction, instruction.asVertex());
			IRATQueryEdgeFrame framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
			framedEdge.setWeight(0);
			framedEdge.setCommandGraphUUID(_commandUUID.toString());
			
			if(!_owners.isEmpty()){
				IRATVertexFrame previousQueryPivotNodeOwner = _owners.pop();
				//System.out.println("Pop: " + previousQueryPivotNodeOwner.getVertexLabelField());
				
				//System.out.println("Link: " + previousQueryPivotNodeOwner.getVertexLabelField() + " -> " + currentQueryPivotNodeOwner.getVertexLabelField());
				edge = previousQueryPivotNodeOwner.asVertex().addEdge(edgeLabel, currentQueryPivotNodeOwner.asVertex());
				framedEdge = _queryGraph.frame(edge, IRATQueryEdgeFrame.class);
				framedEdge.setWeight(0);
				framedEdge.setCommandGraphUUID(_commandUUID.toString());
			}
			
			//System.out.println("Push: " + currentQueryPivotNodeOwner.getVertexLabelField());
			_owners.push(currentQueryPivotNodeOwner);
		}
	}
	
	private IInstructionNodeFrame addInstruction(List<IInstructionParameterNodeFrame>instructionParameters, String commandName){
		IInstructionNodeFrame instruction = _queryGraph.addVertex(null, IInstructionNodeFrame.class);
		instruction.setInstructionOrderField(0);
		instruction.setVertexContentField(commandName);
		instruction.setVertexLabelField(commandName);
		instruction.setVertexRoleValueRootField(false);
		instruction.setVertexTypeField(VertexType.Instruction);
		UUID uuid = UUID.randomUUID();
		instruction.setVertexUUIDField(uuid.toString());
		instruction.setMaxNumParameters(instructionParameters.size());
		
		int inc = 0;
		for(IInstructionParameterNodeFrame instructionParameter : instructionParameters){
			instructionParameter.setInstructionOrderField(inc);
			Edge edge = instruction.asVertex().addEdge(RATConstants.EdgeInstructionParameter, instructionParameter.asVertex());
			IInstructionParameterEdgeFrame framedEdge = _queryGraph.frame(edge, IInstructionParameterEdgeFrame.class);
			framedEdge.setWeight(inc);
			inc++;
		}
		
		return instruction;
	}
	
	private IInstructionParameterNodeFrame addInstructionParameter(String paramName, String paramValue, ReturnType returnType){
		IInstructionParameterNodeFrame instructionParameter = _queryGraph.addVertex(null, IInstructionParameterNodeFrame.class);
		instructionParameter.setVertexUserCommandsInstructionsParameterNameField(paramName);
		instructionParameter.setVertexUserCommandsInstructionsParameterValueField(paramValue);
		instructionParameter.setVertexUserCommandsInstructionsParameterReturnTypeField(returnType);
		instructionParameter.setVertexLabelField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexContentField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexUUIDField(UUID.randomUUID().toString());
		instructionParameter.setVertexRoleValueRootField(false);
		instructionParameter.setVertexTypeField(VertexType.InstructionParameter);
		
		return instructionParameter;
	}
	
	public Vertex get_rootNode() {
		return _rootNode.asVertex();
	}
	
	public UUID get_commandUUID() {
		return _commandUUID;
	}

	public UUID get_rootNodeUUID() {
		return _rootNodeUUID;
	}
	
	public JSONType get_commandType() {
		return _commandType;
	}

	public String getCommandName() {
		return _commandName;
	}

	private void setCommandName(String commandName) {
		this._commandName = commandName;
	}
	
	public Graph getGraph() {
		return _queryGraph;
	}
}
