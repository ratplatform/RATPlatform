/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeQueryPivotFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.InstructionWrapper;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.graphgenerator.node.wrappers.CommandNode;
import com.dgr.rat.graphgenerator.node.wrappers.QueryPivotNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public abstract class AbstractCommand implements ICommandCreator {
	public class QueryPivotNodeManager{
		private Object _commandNode = null;
		private LinkedList<QueryPivotNode>_list = new LinkedList<QueryPivotNode>();
		
		public Object getCommandNode() {
			return _commandNode;
		}

		public void setCommandNode(Object commandNode) {
			this._commandNode = commandNode;
		}
		
		public void addQueryPivot(QueryPivotNode queryPivot){
			_list.add(queryPivot);
		}
		
		public Iterator<QueryPivotNode>getListIterator(){
			return _list.iterator();
		}
		
		public int size(){
			return _list.size();
		}
	};

	private String _commandName = null;
	private UUID _commandUUID = null;
	private UUID _rootNodeUUID = null;
	private JSONType _commandType = null;
	private FramedGraph<Graph> _framedGraph = null;
	private CommandNode _rootNode = null;
	private String _commandVersion = null;
	private String _edgeName = null;
	private Map<String, LinkedList<QueryPivotNodeManager>>_queryPivotNodes = new LinkedHashMap<String, LinkedList<QueryPivotNodeManager>>();
	
	public AbstractCommand(String commandName, String commandVersion) {
		set_commandName(commandName);
		set_commandVersion(commandVersion);
        Graph graph = new TinkerGraph();
        _framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
        _edgeName = commandName;
        String commandUUIDstr = GraphGeneratorHelpers.getUUID(_commandName);
        _commandUUID = UUID.fromString(commandUUIDstr);
	}
	
	public abstract void addNodesToGraph() throws Exception;
	
	private QueryPivotNodeManager find(LinkedList<QueryPivotNodeManager>list, Object node){
		QueryPivotNodeManager result = null;
		for(QueryPivotNodeManager query : list){
			if(query.getCommandNode() == node){
				result = query;
				break;
			}
		}
		
		return result;
	}
	
	protected void setQueryPivot(Object node, String correlationKey, String queryName, boolean isStartPivot) throws Exception{
		this.setQueryPivot(node, correlationKey, queryName, isStartPivot, null);
	}
	
	protected void setQueryPivot(Object node, String correlationKey, String queryName, boolean isStartPivot, String paramName) throws Exception{
		LinkedList<QueryPivotNodeManager>list = null;
		QueryPivotNodeManager queryPivotNodeManager = null;
		if(_queryPivotNodes.containsKey(correlationKey)){
			list = _queryPivotNodes.get(correlationKey);
			queryPivotNodeManager = this.find(list, node);
		}
		else{
			list = new LinkedList<QueryPivotNodeManager>();
			_queryPivotNodes.put(correlationKey, list);
		}
		
		if(queryPivotNodeManager == null){
			queryPivotNodeManager = new QueryPivotNodeManager();
			list.add(queryPivotNodeManager);
		}

		QueryPivotNode queryPivotNode = new QueryPivotNode(isStartPivot);
		queryPivotNode.set_nodeUUID(UUID.randomUUID());
		queryPivotNode.setQueryName(queryName);
		int size = queryPivotNodeManager.size();
		queryPivotNode.set_orderField(size);
		queryPivotNode.set_nodeContent(queryName);
		queryPivotNode.set_correlationKey(correlationKey);
		if(paramName != null){
			queryPivotNode.setParamName(paramName);
		}
		
		queryPivotNodeManager.addQueryPivot(queryPivotNode);
		queryPivotNodeManager.setCommandNode(node);
	}
	
	private void buildQueryPivots() throws Exception{
		Iterator<String>it = _queryPivotNodes.keySet().iterator();
		while(it.hasNext()){
			String correlationKey = it.next();
			LinkedList<QueryPivotNodeManager> list = _queryPivotNodes.get(correlationKey);
			QueryPivotNode lastQueryPivotNode = null;
			
			for(QueryPivotNodeManager queryPivotNodeManager : list){
				Vertex owner = null;
				
				Iterator<QueryPivotNode>qIt = queryPivotNodeManager.getListIterator();
				
				while(qIt.hasNext()){
					QueryPivotNode query = qIt.next();
					
					query.buildNodes(_framedGraph, this.get_commandName(), _commandUUID, _edgeName);
					IRATNodeQueryPivotFrame childNode = query.getNode();
					
					Object obj = queryPivotNodeManager.getCommandNode();
					if(obj instanceof CommandNode){
						CommandNode node = (CommandNode) obj;
						owner = node.getNode().asVertex();
						
						String paramName = query.getParamName();
						if(paramName != null){
							IInstructionParameterNodeFrame instructionParameter = _framedGraph.addVertex(null, IInstructionParameterNodeFrame.class);
							instructionParameter.setVertexUserCommandsInstructionsParameterNameField(paramName);
							instructionParameter.setVertexUserCommandsInstructionsParameterValueField(RATConstants.VertexContentUndefined);
							instructionParameter.setVertexUserCommandsInstructionsParameterReturnTypeField(ReturnType.string);
							instructionParameter.setVertexLabelField(VertexType.InstructionParameter.toString());
							instructionParameter.setVertexContentField(VertexType.InstructionParameter.toString());
							instructionParameter.setVertexUUIDField(UUID.randomUUID().toString());
							instructionParameter.setVertexRoleValueRootField(false);
							instructionParameter.setVertexTypeField(VertexType.InstructionParameter);
							childNode.asVertex().addEdge(RATConstants.QueryPivotEdgeLabel, instructionParameter.asVertex());
						}
					}
					else if(obj instanceof InstructionWrapper){
						String paramName = query.getParamName();
						if(paramName == null){
							throw new Exception();
						}
						InstructionWrapper node = (InstructionWrapper) obj;
						IInstructionParameterNodeFrame param = node.getInstruction().getInstructionParameter(paramName);
						if(param == null){
							throw new Exception();
						}
						owner = param.asVertex();
					}
					else{
						throw new Exception();
					}
					
					owner.addEdge(RATConstants.QueryPivotEdgeLabel, childNode.asVertex());
					if(lastQueryPivotNode != null){
						Vertex last = lastQueryPivotNode.getNode().asVertex();
						Vertex current = query.getNode().asVertex();
						last.addEdge(correlationKey, current);
					}
					lastQueryPivotNode = query;
				}
			}
		}
	}
	
	public FramedGraph<Graph> getFramedGraph(){
		return _framedGraph;
	}
	
	protected UUID createNodeUUID(CommandNode node){
		System.out.println(node.get_nodeContent());
		UUID result = null;
		if(node.isRootNode()){
			String nodeUUIDstr = GraphGeneratorHelpers.getUUID(node.get_nodeContent());
			result = UUID.fromString(nodeUUIDstr);
		}
		else{
			result = UUID.randomUUID();
		}
		
		return result;
	}
	
	public <T extends CommandNode> T buildRootNode(Class<T> cls, String content) throws Exception {
		if(get_rootCommandNode() != null){
			throw new Exception("_rootNode already exists!");
		}
		T node = this.build(cls, true, content);
		set_rootCommandNode(node);
		this.set_rootNodeUUID(node.get_nodeUUID());
		
		return node;
	}
	
	public <T extends CommandNode> T buildNode(Class<T> cls, String content) throws Exception {
        return this.build(cls, false, content);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends CommandNode> T build(Class<T> cls, boolean isRoot, String content) throws Exception {
		T node = null;
		try{
			Class<?> argTypes[] = {AbstractCommand.class, boolean.class, String.class};
	        Constructor<?> ct = cls.getConstructor(argTypes);
	        Object arglist[] = { this, isRoot, content };
	        Object object = ct.newInstance(arglist);
	        node = (T) object;
		}
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
		node.set_nodeUUID(this.createNodeUUID(node));
        return node;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CommandNode> T buildNode(Class<T> cls) throws Exception {
		T node = null;
		try{
			Class<?> argTypes[] = {AbstractCommand.class, boolean.class};
	        Constructor<?> ct = cls.getConstructor(argTypes);
	        Object arglist[] = { this, false };
	        Object object = ct.newInstance(arglist);
	        node = (T) object;
		}
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
		node.set_nodeUUID(this.createNodeUUID(node));
        return node;
	}
	
	public void buildGraph() throws Exception{
		get_rootCommandNode().buildNodes(_framedGraph, this.get_commandName(), _commandUUID, _edgeName);
		this.buildQueryPivots();
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#getGraph()
	 */
	@Override
	public Graph getGraph(){
		return _framedGraph.getBaseGraph();
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_commandUUID()
	 */
	@Override
	public UUID get_commandUUID() {
		return _commandUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_commandName()
	 */
	@Override
	public String get_commandName() {
		return _commandName;
	}

	/**
	 * @param _commandName the _commandName to set
	 */
	protected void set_commandName(String commandName) {
		this._commandName = commandName;
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_commandType()
	 */
	@Override
	public JSONType get_commandType() {
		return _commandType;
	}

	/**
	 * @param _commandType the _commandType to set
	 */
	public void set_commandType(JSONType commandType) {
		this._commandType = commandType;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_rootNodeUUID()
	 */
	@Override
	public UUID get_rootNodeUUID() {
		return _rootNodeUUID;
	}

	/**
	 * @param _rootNodeUUID the _rootNodeUUID to set
	 */
	private void set_rootNodeUUID(UUID rootNodeUUID) {
		this._rootNodeUUID = rootNodeUUID;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_rootNode()
	 */
	public CommandNode get_rootCommandNode() {
		return _rootNode;
	}

	/**
	 * @param _rootNode the _rootNode to set
	 */
	private void set_rootCommandNode(CommandNode rootNode) {
		this._rootNode = rootNode;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.ICommandCreator#get_commandVersion()
	 */
	@Override
	public String get_commandVersion() {
		return _commandVersion;
	}

	/**
	 * @param _commandVersion the _commandVersion to set
	 */
	private void set_commandVersion(String _commandVersion) {
		this._commandVersion = _commandVersion;
	}
	
	public IRATNodeFrame get_rootNode(){
		return get_rootCommandNode().getNode();
	}

	/**
	 * @return the _edgeName
	 */
	public String get_edgeName() {
		return _edgeName;
	}

	/**
	 * @param _edgeName the _edgeName to set
	 */
	public void set_edgeName(String edgeName) {
		this._edgeName = edgeName;
	}
}
