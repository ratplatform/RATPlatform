/**
 * @author: Daniele Grignani
 * @date: Oct 31, 2015
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

public class GetAllDomainComments implements IInstruction{

	public GetAllDomainComments() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String userUUID = invoker.getNodeParamValue("userUUID");
//		VertexType vertexType = VertexType.fromString(type);
		String content = null;
		String contentField = null;
		if(nodeCaller.getVertexType().equals(VertexType.SystemKey)){
			contentField = RATConstants.VertexContentField;
		}
		else{
			contentField = RATConstants.VertexTypeField;
		}
		content = invoker.getNodeParamValue(contentField);
		
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
		pipe.both(edgeLabel).has(contentField, content).both(edgeLabel).has(RATConstants.VertexUUIDField, userUUID).back(4);
		//System.out.println("queryPipe: " + pipe.toString());

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
