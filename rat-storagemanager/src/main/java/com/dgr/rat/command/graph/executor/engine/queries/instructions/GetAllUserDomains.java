/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.List;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class GetAllUserDomains implements IInstruction{

	public GetAllUserDomains() {
		// TODO Auto-generated constructor stub
	}
	
//	public static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
//		@Override
//		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
//			return true;
//		}
//	};
//	
//	public static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> emitFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
//		@Override
//		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
//			Vertex vertex = argument.getObject();
//			if(vertex == null){
////				System.out.println("vertex == null");
//				return false;
//			}
//			Object property = vertex.getProperty(RATConstants.VertexTypeField);
//			if(property == null){
////				System.out.println("property == null");
//				return false;
//			}
//        	boolean result = property.toString().equalsIgnoreCase(VertexType.SystemKey.toString());
//        	System.out.println(property.toString() + ": " + result);
//            return result;
//		}
//	};
//	
//	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
//	private void execute(Vertex commandVertex, UUID rootUUID, IStorage storage, String edgeLabel){
//		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandVertex);
//		List<List> lists = p.out().loop(1, GetAllUserDomains.whileFunction, GetAllUserDomains.emitFunction).path().toList();
//		
//		System.out.println(lists);
//		for(List list: lists){
//			
//			GremlinPipeline<Vertex, Vertex> queryPipe = null;
//			Iterator<Object>it = list.iterator();
//			
//			while(it.hasNext()) {
//				Object obj = it.next();
//				Vertex vertex = (Vertex) obj;
////				System.out.println("Current vertex: " + vertex);
//				
//				VertexType vertexType = VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString());
//				switch(vertexType){
//				case QueryPivot:
//					Vertex rootVertex = storage.getVertex(rootUUID);
//					queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
//					break;
//					
//				case SystemKey:
//					String content = vertex.getProperty(RATConstants.VertexContentField);
//					queryPipe.in(edgeLabel).has(RATConstants.VertexContentField, content);
//					break;
//					
//				default:
//					queryPipe.in(edgeLabel).has(RATConstants.VertexTypeField, vertexType.toString());
//					break;
//						
//				}
//				
////				System.out.println(queryPipe);
//			}
//			Object result = queryPipe.toList();
//			System.out.println(result.toString());
//		}
//	}
//
//	
//	private static final PipeFunction<Edge, Boolean> filterFunction = new PipesFunction<Edge, Boolean>(){
//		@Override
//		public Boolean compute(Edge edge){
//			boolean result = false;
//			Vertex vertex = edge.getVertex(Direction.IN);
//			String content = vertex.getProperty(RATConstants.VertexTypeField);
//			
//			result = content.equalsIgnoreCase(VertexType.SystemKey.toString()) ? true : false;
//			return result;
//		}
//	};
	
	private static final PipeFunction<Vertex, Boolean> userNamefilterFunction = new PipesFunction<Vertex, Boolean>(){
		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String content = vertex.getProperty(RATConstants.VertexTypeField);
			
			result = content.equalsIgnoreCase(VertexType.UserName.toString()) ? true : false;
			return result;
		}
	};
	
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String type = invoker.getParamValue(RATConstants.VertexTypeField);
		VertexType vertexType = VertexType.fromString(type);
		
		String edgeLabel = invoker.getParamValue("edgeLabel");
		
		// COMMENT il nodeCaller non è il nodo che ha generato il valore che mi interessa, ma è il parent di caller
		// ad averlo fatto... Infatti nodeCaller è quello corrente, ossia il nodo al quale è collecata questa instruction.
		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
		InstructionResultContainer commandResponse = invoker.getInstructionResult(nodeUUID);
		if(commandResponse == null){
			throw new Exception();
			// TODO: log
		}
		// COMMENT: in questo caso posso dare per scontato che commandResponse contenga un solo valore
		IInstructionResult instructionResult = commandResponse.pollByInMemoryUUID(nodeUUID);
		if(instructionResult == null){
			throw new Exception();
			// TODO: log
		}
		PipeResult queryResult = instructionResult.getContent(PipeResult.class);
		if(queryResult == null){
			throw new Exception();
			// TODO: log
		}
		GremlinPipeline<Vertex, Vertex> pipe = queryResult.getContent();
		pipe.in(edgeLabel).has(RATConstants.VertexTypeField, vertexType.toString());
		System.out.println(pipe);
		
		List<Vertex> result = pipe.toList();
//		System.out.println(result.toString());

		Graph graph = new TinkerGraph();
		// COMMENT: recupero la rootUUID passata tra i comandi; essa rappresenta il nodo al quale sonop collegati tutti i nodi 
		// ricavati qui
		UUID rootUUID = queryResult.getRootUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		Vertex newRootVertex = graph.addVertex(null);
		for(String key : rootVertex.getPropertyKeys()){
			Object value = rootVertex.getProperty(key);
			newRootVertex.setProperty(key, value);
		}
		
		GremlinPipeline<Vertex, Vertex> userPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		Vertex userNameVertex = userPipe.outE().inV().filter(userNamefilterFunction).next();
		String userName = userNameVertex.getProperty(RATConstants.VertexContentField);
		newRootVertex.setProperty(RATConstants.VertexContentField, userName);
		newRootVertex.setProperty(RATConstants.VertexLabelField, userName);
		
		for(Vertex vertex : result){
			Vertex newVertex = graph.addVertex(null);
			for(String key : vertex.getPropertyKeys()){
				Object value = vertex.getProperty(key);
				newVertex.setProperty(key, value);
			}
			newVertex.setProperty(RATConstants.VertexIsRootField, false);
			newRootVertex.addEdge("", newVertex);
		}
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		
		return resultGraph;
	}
}
