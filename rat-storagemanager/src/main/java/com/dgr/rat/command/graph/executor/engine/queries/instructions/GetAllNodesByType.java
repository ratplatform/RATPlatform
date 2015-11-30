package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.List;
import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.queries.QueryHelpers;
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

public class GetAllNodesByType implements IInstruction{

	public GetAllNodesByType() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		// TODO: errore: non deve essere VertexContentField ma VertexTypeField
		String type = invoker.getNodeParamValue(RATConstants.VertexContentField);
		VertexType vertexType = VertexType.fromString(type);
		
		String edgeLabel = invoker.getNodeParamValue("edgeLabel");
		
		// COMMENT il nodeCaller non è il nodo che ha generato il valore che mi interessa, ma è il parent di caller
		// ad averlo fatto... Infatti nodeCaller è quello corrente, ossia il nodo al quale è collegata questa instruction.
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

		List<Vertex> results = pipe.toList();
//		System.out.println(result.toString());

//		Graph graph = new TinkerGraph();
//		// COMMENT: recupero la rootUUID passata tra i comandi; essa rappresenta il nodo al quale sonop collegati tutti i nodi 
//		// ricavati qui
		UUID rootUUID = queryResult.getRootUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
//		Vertex newRootVertex = graph.addVertex(null);
//		for(String key : rootVertex.getPropertyKeys()){
//			Object value = rootVertex.getProperty(key);
//			newRootVertex.setProperty(key, value);
//		}
//		
//		for(Vertex vertex : result){
//			Vertex newVertex = graph.addVertex(null);
//			for(String key : vertex.getPropertyKeys()){
//				Object value = vertex.getProperty(key);
//				newVertex.setProperty(key, value);
//			}
//			newRootVertex.addEdge("", newVertex);
//			
//			GremlinPipeline<Vertex, Vertex> userPipe = new GremlinPipeline<Vertex, Vertex>(vertex);
//			Vertex userNameVertex = userPipe.outE().inV().filter(filterFunction).next();
//			String userName = userNameVertex.getProperty(RATConstants.VertexContentField);
//			newVertex.setProperty(RATConstants.VertexContentField, userName);
//			newVertex.setProperty(RATConstants.VertexLabelField, userName);
//		}
		Graph graph = QueryHelpers.getResultGraph(rootVertex, results);
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);

		return resultGraph;
	}

}
