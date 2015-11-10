/**
 * @author Daniele Grignani (dgr)
 * @date Oct 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InitDomainResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.util.PipesFunction;

public class InitDomain implements IInstruction{
	protected class FilterFunction extends PipesFunction<Vertex, Boolean>{
		private String _filter = null;
		public FilterFunction(String filter) {
			_filter = filter;
		}

		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String content = vertex.getProperty(RATConstants.VertexContentField);
			
			result = content.equalsIgnoreCase(_filter) ? true : false;
			return result;
		}
	}
	
	protected class KeyFunction extends PipesFunction<Vertex, Vertex>{
		private FilterFunction _filter = null;
		public KeyFunction(FilterFunction filter) {
			_filter = filter;
		}
		@Override
		public Vertex compute(Vertex vertex){
			GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(vertex);
			return pipe.filter(_filter).next();
		}
	}
	
	public InitDomain() {
		// TODO Auto-generated constructor stub
	}
	
	public static final PipesFunction<Vertex, Iterable<Vertex>> valueFunction = new PipesFunction<Vertex, Iterable<Vertex>>(){
		@Override
		public Iterable<Vertex> compute(Vertex vertex){
			GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(vertex);
			
			return pipe.outE().inV();
		}
	};

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstruction#execute(com.dgr.rat.command.graph.executor.engine.IInstructionInvoker, com.dgr.rat.command.graph.executor.engine.ICommandProxyNodeVisitable)
	 */
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		IStorage storage = invoker.getStorage();
		
		InitDomainResult result = new InitDomainResult(nodeCaller.getInMemoryNodeUUID());
		this.duplicateCommands(storage, nodeCaller, RATConstants.Commands, result);
		this.duplicateCommands(storage, nodeCaller, RATConstants.Queries, result);
		
		return result;
	}
	
	private void duplicateCommands(final IStorage storage, final ICommandNodeVisitable nodeCaller, final String filter, InitDomainResult resultOut) throws Exception {
		UUID uuid = nodeCaller.getStoredNodeUUID();
		Vertex newDomainVertex = storage.getVertex(uuid);
		if(newDomainVertex == null){
			throw new Exception();
			// TODO log
		}
		
		String commandName = nodeCaller.getCommandName();
		
		Vertex rootDomain = storage.getRootDomain();
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(rootDomain);
//		System.out.println(list.size());

		FilterFunction filterFunction = new FilterFunction(filter);
		@SuppressWarnings("unchecked")
		Map<Vertex, List<Vertex>> map = (Map<Vertex, List<Vertex>>) pipe.outE().inV().filter(filterFunction).groupBy(
				new KeyFunction(filterFunction), valueFunction).cap().next();
//		System.out.println(map.size());
		if(map == null || map.size() != 1){
			throw new Exception();
			// TODO log
		}
		
		// COMMENT: sono sicuro che ci sia un solo elemento
		Set<Entry<Vertex, List<Vertex>>> entry = map.entrySet();
		Vertex commandsVertex = entry.iterator().next().getKey();
		List<Vertex> commandVertices = entry.iterator().next().getValue();
		if(commandVertices == null || commandVertices.size() == 0){
			throw new Exception();
			// TODO log
		}
		
		Vertex newCommandsVertex = storage.addVertex();
		RATHelpers.duplicateVertex(commandsVertex, true, newCommandsVertex);
		
		for(Vertex command : commandVertices){
			Vertex newVertex = storage.addVertex();
			RATHelpers.duplicateVertex(command, true, newVertex);
			newCommandsVertex.addEdge(filter, newVertex);
		}
		
		newDomainVertex.addEdge(commandName, newCommandsVertex);
		
//		storage.commit();
		
//		UUID inrMemoryNodeUUID = nodeCaller.getInMemoryNodeUUID();
//		InstructionResult commandResult = new InstructionResult();
//		commandResult.setNewObjectUUID((UUID) newCommandsVertex.getProperty(RATConstants.VertexUUIDField));
//		commandResult.setInMemoryNodeUUID(inrMemoryNodeUUID);
		
		String uuidStr = newCommandsVertex.getProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(uuidStr)){
			throw new Exception();
			// TODO log
		}
		
		resultOut.setUUID(filter, UUID.fromString(uuidStr));
		
//		return commandResult;
	}
}
