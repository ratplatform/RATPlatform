package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class GetWebDoc implements IInstruction{

	public GetWebDoc() {
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("deprecation")
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String url = invoker.getNodeParamValue("url");
		String urlMD5 = Utils.getMD5(url);
		System.out.println("urlMD5: " + urlMD5);
		
		String content = VertexType.SystemKey.toString();
		String contentField = RATConstants.VertexTypeField;
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
		//pipe.both(edgeLabel).has(contentField, content);//.has("url", url);
		pipe.both(edgeLabel).has(contentField, content).out(edgeLabel).has(RATConstants.VertexContentField, urlMD5).back(2);//.both(edgeLabel).has(RATConstants.VertexContentField, url);
		//System.out.println("queryPipe: " + pipe.toString());
		//List<Vertex> result = pipe.toList();

		UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
		PipeResult newQueryResult = new PipeResult(nodeCallerInMemoryUUID);
		newQueryResult.setContent(pipe);
		newQueryResult.setRootUUID(queryResult.getRootUUID());
		
		return newQueryResult;
		
//		String edgeLabel = invoker.getNodeParamValue("edgeLabel");
//		
//		// COMMENT il nodeCaller non è il nodo che ha generato il valore che mi interessa, ma è il parent di caller
//		// ad averlo fatto... Infatti nodeCaller è quello corrente, ossia il nodo al quale è collecata questa instruction.
//		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
//		InstructionResultContainer commandResponse = invoker.getInstructionResult(nodeUUID);
//		if(commandResponse == null){
//			throw new Exception();
//			// TODO: log
//		}
//		// COMMENT: in questo caso posso dare per scontato che commandResponse contenga un solo valore
//		IInstructionResult instructionResult = commandResponse.pollByInMemoryUUID(nodeUUID);
//		if(instructionResult == null){
//			throw new Exception();
//			// TODO: log
//		}
//		PipeResult queryResult = instructionResult.getContent(PipeResult.class);
//		if(queryResult == null){
//			throw new Exception();
//			// TODO: log
//		}
//		GremlinPipeline<Vertex, Vertex> pipe = queryResult.getContent();
//		pipe.both(edgeLabel).has("url", url);
//
//		UUID nodeCallerInMemoryUUID = nodeCaller.getInMemoryNodeUUID();
//		PipeResult newQueryResult = new PipeResult(nodeCallerInMemoryUUID);
//		newQueryResult.setContent(pipe);
//		newQueryResult.setRootUUID(queryResult.getRootUUID());
//		
////		System.out.println("nodeUUID: " + nodeUUID);
////		System.out.println("nodeCallerInMemoryUUID: " + nodeCallerInMemoryUUID);
////		System.out.println("queryResult.getRootUUID(): " + queryResult.getRootUUID());
//		
//		return newQueryResult;
	}
}
