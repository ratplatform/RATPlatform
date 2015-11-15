/**
 * @author: Daniele Grignani
 * @date: Nov 15, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.ArrayList;
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

public class GetUserDomainByName implements IInstruction{

	public GetUserDomainByName() {
		// TODO Auto-generated constructor stub
	}

	private static final PipeFunction<Vertex, Boolean> userNamefilterFunction = new PipesFunction<Vertex, Boolean>(){
		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String content = vertex.getProperty(RATConstants.VertexTypeField);
			
			result = content.equalsIgnoreCase(VertexType.UserName.toString()) ? true : false;
			return result;
		}
	};
	
//	private static final PipeFunction<Vertex, Vertex> domainNamefilterFunction = new PipesFunction<Vertex, Vertex>(){
//		@Override
//		public Vertex compute(Vertex vertex){
//			String domainName = vertex.getProperty(RATConstants.VertexContentField);
//			return vertex.getProperty("name");
//			
//			boolean result = false;
//			String content = vertex.getProperty(RATConstants.VertexTypeField);
//			
//			result = content.equalsIgnoreCase(VertexType.UserName.toString()) ? true : false;
//			return result;
//		}
//	};
	
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String domainName = invoker.getNodeParamValue("domainName");
		if(domainName == null || domainName.length() < 1){
			throw new Exception();
			// TODO log
		}
		
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
		List<Vertex> list = new ArrayList<Vertex>();
		GremlinPipeline<Vertex, Vertex> pipe = queryResult.getContent();
		pipe.in(edgeLabel).has(RATConstants.VertexTypeField, vertexType.toString());
		System.out.println(pipe);
		for (Object o : pipe){
			System.out.println(o.toString());
		}
//		Graph graph = new TinkerGraph();
//		return null;
		
//		List<Vertex> result = pipe.toList();
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
		
//		for(Vertex vertex : result){
//			Vertex newVertex = graph.addVertex(null);
//			for(String key : vertex.getPropertyKeys()){
//				Object value = vertex.getProperty(key);
//				newVertex.setProperty(key, value);
//			}
//			newVertex.setProperty(RATConstants.VertexIsRootField, false);
//			newRootVertex.addEdge("", newVertex);
//		}
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		
		return resultGraph;
	}
//	@Override
//	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
//		String domainName = invoker.getParamValue("domainName");
//		return null;
//	}

}
