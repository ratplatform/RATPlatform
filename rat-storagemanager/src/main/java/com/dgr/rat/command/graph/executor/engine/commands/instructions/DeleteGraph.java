package com.dgr.rat.command.graph.executor.engine.commands.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.IInstruction;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResult;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.PipesFunction;

public class DeleteGraph implements IInstruction{
	private class EmitFunctionClass extends PipesFunction<LoopPipe.LoopBundle<Vertex>, Boolean>{
		private String _filter = null;
		private String _property = null;
		
		public EmitFunctionClass(String property, String filter) {
			_property = property;
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
			Object property = vertex.getProperty(_property);
			if(property == null){
//				System.out.println("property == null");
				return false;
			}
			
        	boolean result = property.toString().equalsIgnoreCase(_filter);
            return result;
		}
	}
	
	public DeleteGraph() {
		// TODO Auto-generated constructor stub
	}
	
	private void removeSubNodes(final Vertex vertex){
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(vertex);
		EmitFunctionClass emitFunctionClass = new EmitFunctionClass(RATConstants.VertexIsRootField, "true");
		List<Vertex> list = (List<Vertex>) pipe.out().loop(1, DeleteGraph.whileFunction, emitFunctionClass).has("subNodes").toList();
		for(Vertex v : list){
			Object subNodes = v.getProperty("subNodes");
			if(subNodes != null){
				int num = Integer.parseInt(subNodes.toString());
				if(num > 0){
					v.setProperty("subNodes", --num);
				}
			}
		}
		System.out.println(list.size());
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public IInstructionResult execute(IInstructionInvoker invoker, ICommandNodeVisitable nodeCaller) throws Exception {
		Object paramValue = invoker.getNodeParamValue("rootNodeUUID");
		if(paramValue == null){
			throw new Exception();
			// TODO: log
		}
		
		List<Vertex>list = invoker.getStorage().getVertices("rootverticesindex", RATConstants.GraphUUID, paramValue);
		Vertex rootVertex = null;
		if(list.isEmpty()){
			// TODO da sistemare
			throw new Exception();
		}
		else{
			rootVertex = list.get(0);
		}

		Object graphUUID = rootVertex.getProperty(RATConstants.GraphUUID);
		EmitFunctionClass emitFunctionClass = new EmitFunctionClass(RATConstants.GraphUUID, graphUUID.toString());
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		List<List> lists = pipe.out().loop(1, DeleteGraph.whileFunction, emitFunctionClass).path().toList();
		List<Vertex>deleted = new ArrayList<Vertex>();
		for(List path: lists){
			int size = path.size();
			int inc = 0;
			for(Object obj : path){
				Vertex v = (Vertex) obj;
				if(!deleted.contains(v)){
					v.setProperty(RATConstants.IsDeleted, true);
					deleted.add(v);
				}
				
				if(inc == size - 1){
					this.removeSubNodes(v);
				}
				inc++;
			}
		}
		
		String strUUID = rootVertex.getProperty(RATConstants.VertexUUIDField);
		UUID rootUUID = UUID.fromString(strUUID);
		
		InstructionResult commandResult = new InstructionResult(rootUUID);
		commandResult.setNewObjectUUID(rootUUID);
		
		return commandResult;
	}
	
	private static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			return true;
		}
	};
}
