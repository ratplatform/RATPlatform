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
import com.dgr.rat.command.graph.executor.engine.queries.QueryHelpers;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.queries.PipeResult;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class GetUserByEmail implements IInstruction{

	public GetUserByEmail() {
		// TODO Auto-generated constructor stub
	}

	private static final PipeFunction<Vertex, Boolean> userNamefilterFunction = new PipesFunction<Vertex, Boolean>(){
		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String content = vertex.getProperty(RATConstants.VertexTypeField);
			
			result = content.equalsIgnoreCase(VertexType.Properties.toString()) ? true : false;
			return result;
		}
	};
	
	// TODO: è possibile ridurre il numero di instruction delle query rivedendo il modello dei parametri; ad esempio
	// potrebbero essere numerati ed il parametro 0 potrebbe essere quello di default, per così dire, in modo 
	// da poterlo invocare senza bisogno di indicarlo esplicitamente, come nel caso di "domainName"
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		String domainName = invoker.getNodeParamValue("domainName");
		if(domainName == null || domainName.length() < 1){
			throw new Exception();
			// TODO log
		}
		
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
		@SuppressWarnings("unchecked")
		List<Vertex> result = (List<Vertex>) pipe.in(edgeLabel).has(RATConstants.VertexContentField, domainName).toList();
		UUID rootUUID = queryResult.getRootUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		
		Graph graph = QueryHelpers.getResultGraph(rootVertex, result, userNamefilterFunction);
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		
		return resultGraph;
	}

}
