package org.rat.platform.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.rat.platform.command.nodes.CommandNode;
import org.rat.platform.command.nodes.RootNode;
import org.rat.platform.graph.visitor.IVisitor;
import org.rat.platform.rat_graph_generator.GraphGeneratorHelpers;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class CommandGraph {
	private CommandNode _rootNode;
	private String _commandName = null;
	private String _uuid = null;
	private TinkerGraph _graph = new TinkerGraph();
	private Map<CommandNode, Vertex> _nodes = new HashMap<CommandNode, Vertex>();
	private List<CommandNode>_visited = new ArrayList<CommandNode>();
	
	private JSONType _commandType = null;
	private String _commandVersion = null;
	
	public CommandGraph(CommandNode rootNode, String commandName) {
		_rootNode = rootNode;
		setCommandName(commandName);
		setUuid(GraphGeneratorHelpers.getUUID(getCommandName()));
	}
	
	public void addNodeAlreadyVisited(CommandNode node){
		_visited.add(node);
	}
	
	public boolean alreadyVisited(CommandNode node){
		return _visited.contains(node);
	}
	
	public Graph getGraph(){
		return _graph;
	}

	public String getUuid() {
		return _uuid;
	}

	private void setUuid(String uuid) {
		this._uuid = uuid;
	}

	public String getCommandName() {
		return _commandName;
	}

	private void setCommandName(String commandName) {
		this._commandName = commandName;
	}
	
	public void addNodeToGraph(CommandNode node){
		//System.out.println("Node: " + node.getContent());
		
		Vertex vertex = _graph.addVertex(null);
		Iterator<String>it = node.getCommandNodePropertiesIt();
		while(it.hasNext()){
			String key = it.next();
			Object value = node.getCommandNodeProperty(key);
			vertex.setProperty(key, value);
		}
		
		_nodes.put(node, vertex);
	}
	
	public void addEdge(CommandNode fromNode, CommandNode toNode, String edgeLabel){
		Vertex from = _nodes.get(fromNode);
		Vertex to = _nodes.get(toNode);
		Edge edge = _graph.addEdge(null, from, to, edgeLabel);
		Object weight = toNode.getCommandNodeProperty(RATConstants.InstructionOrderField);
		if(weight != null){
			edge.setProperty("weight", weight);
		}
	}
	
	public void addInstructionToNode(CommandNode node){
		System.out.println("Instructions: " + node.getContent());
	}
	
	public void run(){
		_rootNode.run(this, _uuid);
	}
	
	public UUID get_commandUUID() {
		return UUID.fromString(_uuid);
	}

	public String get_commandName() {
		return _commandName;
	}

	protected void set_commandName(String commandName) {
		this._commandName = commandName;
	}
	
	public JSONType get_commandType() {
		return _commandType;
	}

	public void set_commandType(JSONType commandType) {
		this._commandType = commandType;
	}

	public UUID get_rootNodeUUID() {
		return UUID.fromString(_rootNode.getUuid());
	}


	public String get_commandVersion() {
		return _commandVersion;
	}

	public void set_commandVersion(String commandVersion) {
		this._commandVersion = commandVersion;
	}
	
	public void accept(IVisitor visitor) {
		_rootNode.accept(visitor);
	}
}
