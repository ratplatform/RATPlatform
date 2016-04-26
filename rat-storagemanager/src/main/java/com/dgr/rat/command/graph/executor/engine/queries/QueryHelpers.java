package com.dgr.rat.command.graph.executor.engine.queries;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class QueryHelpers {
	//@SuppressWarnings("unchecked")
	public static Graph getResultGraph(Vertex rootVertex, final List<Vertex> results) throws Exception{
		Graph graph = new TinkerGraph();
		// COMMENT: recupero la rootUUID passata tra i comandi; essa rappresenta il nodo al quale sonop collegati tutti i nodi 
		// ricavati qui
		Vertex newRootVertex = graph.addVertex(null);
		RATHelpers.duplicateVertex(rootVertex, false, newRootVertex);
		
//		GremlinPipeline<Vertex, Vertex> userPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
//		Vertex userNameVertex = userPipe.outE().inV().filter(filterFunction).next();
//		String userName = userNameVertex.getProperty(RATConstants.VertexContentField);
//		newRootVertex.setProperty(RATConstants.VertexContentField, userName);
//		newRootVertex.setProperty(RATConstants.VertexLabelField, userName);
		
		for(Vertex vertex : results){
			Vertex newVertex = graph.addVertex(null);
			RATHelpers.duplicateVertex(vertex, false, newVertex);
			
			// COMMENT prenso il nodo di tipo Properties, se c'è.
//			GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(vertex);
//			List<Vertex> properties = (List<Vertex>) pipe.both().has(RATConstants.VertexTypeField, VertexType.Properties.toString()).toList();
//			if(results.size() > 1){
//				throw new Exception();
//				// TODO log
//			}
//			if(!properties.isEmpty()){
//				Vertex property = properties.get(0);
//				Vertex newProperty = graph.addVertex(null);
//				RATHelpers.duplicateVertex(property, false, newProperty);
//				newVertex.addEdge("", newProperty);
//			}
//			System.out.println(properties.toString());
			
			newVertex.setProperty(RATConstants.VertexIsRootField, false);
			newRootVertex.addEdge("", newVertex);
		}
		
		return graph;
	}
	
	public static IInstructionResult startPipe(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String rootDomainUUID = invoker.getNodeParamValue("rootNodeUUID");
		
		UUID rootUUID = UUID.fromString(rootDomainUUID);
		
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		
		UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
		PipeResult queryResult = new PipeResult(nodeCallerInMemoryUUID);
		queryResult.setContent(queryPipe);
		
		// COMMENT: setto la rootUUID che poi mi servirà nell'ultimo nodo eseguito: ExecuteQueryPipe
		queryResult.setRootUUID(rootUUID);
		
		return queryResult;
	}
	
	public static IInstructionResult executePipe(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String type = invoker.getNodeParamValue(RATConstants.VertexTypeField);
		VertexType vertexType = VertexType.fromString(type);
		
		String edgeLabel = invoker.getNodeParamValue("edgeLabel");
		
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
