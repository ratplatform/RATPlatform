package com.rat.command.graph.executor.engine.queries.instructions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.PipesFunction;

public class GetGraph implements IInstruction{
	private class EmitFunctionClass extends PipesFunction<LoopPipe.LoopBundle<Vertex>, Boolean>{
		private String _filter = null;
		
		public EmitFunctionClass(String filter) {
			_filter = filter;
		}

		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			Vertex vertex = argument.getObject();
			if(vertex == null){
//				System.out.println("vertex == null");
				return false;
			}
			//System.out.println("vertex: " + vertex.getProperty(RATConstants.VertexLabelField));
			Object property = vertex.getProperty(RATConstants.GraphUUID);
			if(property == null){
//				System.out.println("property == null");
				return false;
			}
			
        	boolean result = property.toString().equalsIgnoreCase(_filter);
            return result;
		}
	}
	
	public GetGraph() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String paramName = invoker.getNodeParamValue("paramName");
		if(paramName == null){
			throw new Exception();
			// TODO: log
		}
		Object paramValue = invoker.getNodeParamValue("paramValue");
		if(paramValue == null){
			throw new Exception();
			// TODO: log
		}
		
		List<Vertex>list = invoker.getStorage().getVertices("rootverticesindex", paramName, paramValue);
		Vertex rootVertex = null;
		if(list.isEmpty()){
			// TODO da sistemare
			throw new Exception();
//			Graph graph = new TinkerGraph();
//			rootVertex = graph.addVertex(null);
//			rootVertex.setProperty(RATConstants.VertexTypeField, VertexType.Empty);
//			rootVertex.setProperty(RATConstants.VertexContentField, VertexType.Empty.toString());
//			rootVertex.setProperty(RATConstants.VertexLabelField, VertexType.Empty.toString());
//			rootVertex.setProperty(RATConstants.VertexUUIDField, UUID.randomUUID().toString());
		}
		else{
			rootVertex = list.get(0);
		}
		
		Object graphUUID = rootVertex.getProperty(RATConstants.GraphUUID);
		EmitFunctionClass emitFunctionClass = new EmitFunctionClass(graphUUID.toString());
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		List<List> lists = pipe.out().loop(1, GetGraph.whileFunction, emitFunctionClass).path().toList();
		//System.out.println(lists.size());
		Graph graph = new TinkerGraph();
		Vertex newRootVertex = graph.addVertex(null);
		RATHelpers.duplicateVertex(rootVertex, false, newRootVertex);
		
		//System.out.println(rootVertex.getId() + "->" + newRootVertex.getId());
		Map<String, Vertex>added = new HashMap<String, Vertex>();
		added.put(rootVertex.getId().toString(), newRootVertex);
		
		for(List path: lists){
			Vertex parent = newRootVertex;
			for(Object obj : path){
				Vertex v = (Vertex) obj;
				Vertex newVertex = null;
				if(added.containsKey(v.getId().toString())){
					newVertex = added.get(v.getId().toString());
				}
				else{
					newVertex = graph.addVertex(null);
					added.put(v.getId().toString(), newVertex);
					RATHelpers.duplicateVertex(v, false, newVertex);
					String label = parent.getProperty(RATConstants.GraphCommandOwner);
							
					parent.addEdge(label, newVertex);
				}

				parent = newVertex;
			}
		}
		
		String strUUID = rootVertex.getProperty(RATConstants.VertexUUIDField);
		UUID rootUUID = UUID.fromString(strUUID);
		
		QueryResult resultGraph = new QueryResult(rootUUID);
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		return resultGraph;
	}
	
	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			return true;
		}
	};

}
