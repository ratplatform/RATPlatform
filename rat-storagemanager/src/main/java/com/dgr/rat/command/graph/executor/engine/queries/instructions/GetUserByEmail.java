/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries.instructions;

import java.util.Iterator;
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
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

// TODO: da cambiare nome in GetNodeByParam
public class GetUserByEmail implements IInstruction{

	public GetUserByEmail() {
	}
	
	// TODO: è possibile ridurre il numero di instruction delle query rivedendo il modello dei parametri; ad esempio
	// potrebbero essere numerati ed il parametro 0 potrebbe essere quello di default, per così dire, in modo 
	// da poterlo invocare senza bisogno di indicarlo esplicitamente, come nel caso di "domainName"
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		// COMMENT in questo caso devo avere un solo parametro disponibile
		Iterator<String>it = invoker.getParameterNameIterator();
		String paramName = it.next();
		if(paramName == null || paramName.length() < 1){
			throw new Exception();
			// TODO log
		}
		String paramValue = invoker.getNodeParamValue(paramName);
		if(paramValue == null || paramValue.length() < 1){
			throw new Exception();
			// TODO log
		}
//		String email = invoker.getNodeParamValue("userEmail");
//		if(email == null || email.length() < 1){
//			throw new Exception();
//			// TODO log
//		}
		
		// COMMENT il nodeCaller non è il nodo che ha generato il valore che mi interessa, ma è il parent di caller
		// ad averlo fatto... Infatti nodeCaller è quello corrente, ossia il nodo al quale è collegata questa instruction.
//		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
		
		// COMMENT: in questo caso non chiamo  nodeCaller.getParent().getInMemoryNodeUUID(); ma nodeCaller.getInMemoryNodeUUID();
		// perché il nodo corrente è il secondo nodo del nodo nodeCaller.getParent() (ossia nodeCaller.getParent() ha due instruction), 
		// quindi i parametri che mi interessano non sono stati inseriti nodeCaller.getParent(), ma da nodeCaller (vedi il grafo del comando)
		// TODO: chiaramente tutto ciò è da semplificare....
		UUID nodeUUID = nodeCaller.getInMemoryNodeUUID();
//		UUID nodeUUID = nodeCaller.getParent().getInMemoryNodeUUID();
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
		List<Vertex> results = (List<Vertex>) pipe.has(paramName, paramValue).toList();
		// COMMENT in questo caso deve avere 0 o 1 elemento
		if(results.size() > 1){
			throw new Exception();
			// TODO log
		}
		
		UUID rootUUID = queryResult.getRootUUID();
		Vertex rootVertex = invoker.getStorage().getVertex(rootUUID);
		
		Graph graph = QueryHelpers.getResultGraph(rootVertex, results);
		
		QueryResult resultGraph = new QueryResult(queryResult.getInMemoryOwnerNodeUUID());
		resultGraph.setRootUUID(rootUUID);
		resultGraph.setGraph(graph);
		
		invoker.addCommandResponse(nodeCaller, resultGraph);
		
		return resultGraph;
	}

}
