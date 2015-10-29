/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.graphgenerator.node.wrappers.CommandNode;
import com.dgr.rat.graphgenerator.node.wrappers.QueryPivotNode;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public abstract class AbstractCommand implements ICommandCreator {
	private String _commandName = null;
	private UUID _commandUUID = null;
	private UUID _rootNodeUUID = null;
	private JSONType _commandType = null;
	private FramedGraph<Graph> _framedGraph = null;
	private CommandNode _rootNode = null;
	private String _commandVersion = null;
	private String _edgeName = null;
	
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

	protected void setQueryPivot(CommandNode node, VertexType fromNodeType, VertexType towardNodeType, String startPipeInstruction, String internalPipeInstruction, String endPipeInstruction) throws Exception{
		// TODO: correggere in seconda battuta: ho il problema che se ho diversi nomi di parametri uguali,
		// quando setto i parametri non so quale scegliere
		QueryPivotNode queryPivotNode = this.buildQueryNode();
		queryPivotNode.set_nodeContent(startPipeInstruction);
		queryPivotNode.setStartPipeInstruction(startPipeInstruction);
		queryPivotNode.setInternalPipeInstruction(internalPipeInstruction);
		queryPivotNode.setEndPipeInstruction(endPipeInstruction);
		queryPivotNode.setTowardNode(towardNodeType);
		queryPivotNode.setFromNode(fromNodeType);
		
		queryPivotNode.setQueryName(startPipeInstruction);
		node.addQueryPivot(queryPivotNode);
	}
	
	public FramedGraph<Graph> getFramedGraph(){
		return _framedGraph;
	}
	
	private UUID createNodeUUID(CommandNode node){
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
	
	private QueryPivotNode buildQueryNode() throws Exception {
		QueryPivotNode node = null;
		try{
			Class<QueryPivotNode> cls = QueryPivotNode.class;
			Class<?> argTypes[] = {};
	        Constructor<?> ct = cls.getConstructor(argTypes);
	        Object arglist[] = { };
	        Object object = ct.newInstance(arglist);
	        node = (QueryPivotNode) object;
		}
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
		node.set_nodeUUID(UUID.randomUUID());
        return node;
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
