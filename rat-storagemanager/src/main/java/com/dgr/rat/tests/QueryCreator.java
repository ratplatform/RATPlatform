/**
 * @author Daniele Grignani (dgr)
 * @date Oct 17, 2015
 */

package com.dgr.rat.tests;

import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class QueryCreator {
	protected class FilterFunction extends PipesFunction<Vertex, Boolean>{
		private Vertex _vertex = null;
		
		public FilterFunction(Vertex vertex) {
			_vertex = vertex;
		}
		
		@Override
		public Boolean compute(Vertex vertex) {
			boolean result = false;
			String uuid = vertex.getProperty(RATConstants.VertexUUIDField).toString();

			result = uuid.equalsIgnoreCase(_vertex.getProperty(RATConstants.VertexUUIDField).toString()) ? true : false;
			return result;
		}
		
	}
	private Stack<IRATNodeFrame>_stack = new Stack<IRATNodeFrame>();
	private GremlinPipeline<Vertex, Vertex> _pipe = null;
	private UUID _rootDomainUUID = null;
	private IStorage _storage = null;
	
	public QueryCreator (IStorage storage, UUID rootDomainUUID){
		_storage = storage;
		_rootDomainUUID = rootDomainUUID;
		
	}
	
	public void addVertex(IRATNodeFrame node) throws Exception{
//		String edgeUUID = null;
		System.out.println("Current vertex: " + node.asVertex());
		IRATNodeFrame nodeFromStack = null;
		if(!_stack.isEmpty()){
			nodeFromStack = _stack.pop();
			System.out.println("Vertex from stack: " + nodeFromStack.asVertex());
			_stack.push(node);
//			edgeUUID = this.getGraphCommandUUIDFromEdge(nodeFromStack, node);
		}
		else {
			// COMMENT: il primo node è il nodo di tipo QueryPivot il cui inE è privo di proprietà.
			// Rappresenta il nodo root da cui parte la query, es.: un nodo di tipo domain, user, RAT, etc.
			Vertex rootVertex = _storage.getVertex(_rootDomainUUID);
			_stack.push(RATHelpers.getRATNodeFrameFromVertex(rootVertex));
			_pipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		}
		
		VertexType vertexType = node.getVertexTypeField();
		switch(vertexType){
		case QueryPivot:
//			Vertex rootVertex = _storage.getVertex(_rootDomainUUID);
//			_pipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
			
			break;
			
		case SystemKey:
//			GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge>(nodeFromStack.asVertex());
//			pipe.outE(RATConstants.EdgeUUIDField, edgeUUID);
//			GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(nodeFromStack.asVertex());
//			pipe.inE().outV();
//			_pipe.add(pipe);
			_pipe.inE().out(RATConstants.VertexTypeField, node.getVertexTypeField().toString());
			break;
			
			default:
				_pipe.inE().out(RATConstants.VertexTypeField, nodeFromStack.getVertexTypeField().toString());
		}
//		_stack.push(node);
	}
	
	@SuppressWarnings("rawtypes")
	public void execute(){
		List list = _pipe.toList();
		System.out.println(list.size());
	}
	
	private String getGraphCommandUUIDFromEdge(IRATNodeFrame from, IRATNodeFrame to) throws Exception{
		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge>(from.asVertex());
		FilterFunction f = new FilterFunction(to.asVertex());
		Edge edge = pipe.inE().outV().filter(f).outE().next();
		String edgeUUID = edge.getProperty(RATConstants.EdgeUUIDField);
		if(!Utils.isUUID(edgeUUID)){
			throw new Exception();
			// TODO log
		}
		
		System.out.println(edge);
		Set<String> properties = edge.getPropertyKeys();
		for(String property : properties){
			System.out.println(property + ": " + edge.getProperty(property));
		}
		
		return edgeUUID;
	}
}
