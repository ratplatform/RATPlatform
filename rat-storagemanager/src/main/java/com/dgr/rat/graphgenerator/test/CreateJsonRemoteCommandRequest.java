/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.graphgenerator.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class CreateJsonRemoteCommandRequest {
	private Map<String, RemoteParameter> _parameters = new HashMap<String, RemoteParameter>();
	private List<IRATFrame>_alreadyExplored = new ArrayList<IRATFrame>();
	
	public CreateJsonRemoteCommandRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public String makeRemoteRequest(IRATNodeFrame root) throws Exception{
		this.makeRemoteClientCommand(root);

		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(_parameters);
		
		return output;
	}
	
	public Map<String, RemoteParameter> getParameters(){
		return _parameters;
	}
	
	private void makeRemoteClientCommand(IRATNodeFrame rootNode) throws Exception{
		if(!_alreadyExplored.contains(rootNode)){
			_alreadyExplored.add(rootNode);
			
			if(this.containsUnknownParameter(rootNode)){
				int num = (int) rootNode.getNumberOfInstructions();
				for(int inc = 0; inc < num; inc++){
					IInstructionNodeFrame instruction = rootNode.getInstruction(inc);
					if(this.containsUnknownParameter(instruction)){
						this.getRemoteClientCommandInstructions(instruction);
					}
				}
			}
			
			// COMMENT: vedi note in CommandNode.buildChildren
//			Iterable<Edge> edgeIterable = rootNode.asVertex().getEdges(Direction.OUT);
//			if(edgeIterable == null){
//				throw new Exception();
//				// TODO: log?
//			}
//			Iterator<Edge>it = edgeIterable.iterator();
//			while(it.hasNext()){
//				Edge edge = it.next();
//				Vertex vertex = edge.getVertex(Direction.IN);
//				if(vertex == null){
//					throw new Exception();
//					// TODO: log?
//				}
//				this.makeRemoteClientCommand(RATHelpers.getRATNodeFrameFromVertex(vertex));
//			}
			// COMMENT: idem come sopra: vedi nota in CommandNode.buildChildren. NON RIMUOVERE!
			Iterator<IRATNodeEdgeFrame> it = rootNode.getAdjacentVertices().iterator();
			while(it.hasNext()){
				IRATNodeEdgeFrame edge = it.next();
				IRATNodeFrame child = edge.getInRatNode();
				this.makeRemoteClientCommand(child);
			}
		}
	}
	
	private void getRemoteClientCommandInstructions(IInstructionNodeFrame instruction) throws Exception{
		long num = instruction.getNumberOfInstructionParameters();
		for(int inc = 0; inc < num; inc++){
			IInstructionParameterNodeFrame param = instruction.getInstructionParameter(inc);
			this.getInstructionParameters(param);
		}
	}
	
	private void getInstructionParameters(IInstructionParameterNodeFrame param) throws Exception{
		RemoteParameter parameter = new RemoteParameter();
		parameter.setInstructionOrder(param.getInstructionOrderField());
		parameter.setParameterName(param.getVertexUserCommandsInstructionsParameterNameField());
		parameter.setParameterValue(param.getVertexUserCommandsInstructionsParameterValueField());
		parameter.setReturnType(param.getVertexUserCommandsInstructionsParameterReturnTypeField());
		parameter.setVertexUUIDField(param.getVertexUUIDField());
//		_parameters.put(param.getVertexUUIDField(), parameter);
		if(_parameters.containsKey(param.getVertexUserCommandsInstructionsParameterNameField())){
			throw new Exception();
		}
		_parameters.put(param.getVertexUserCommandsInstructionsParameterNameField(), parameter);
	}
	
	private static final PipeFunction<Vertex, Boolean> isParam = new PipesFunction<Vertex, Boolean>(){
		@Override
		public Boolean compute(Vertex vertex){
			boolean result = false;
			String paramValue = vertex.getProperty(RATConstants.VertexInstructionParameterValueField);
			
			result = RATConstants.VertexContentUndefined.equals(paramValue) ? true : false;
			return result;
		}
	};
	
	private boolean containsUnknownParameter(IRATNodeFrame node){
		Vertex v = node.asVertex();
		GremlinPipeline<Vertex, Vertex>pipe = new GremlinPipeline<Vertex, Vertex>(v);
		List<Vertex> list = pipe.outE(RATConstants.EdgeInstruction).inV()
				.outE(RATConstants.EdgeInstructionParameter).inV().filter(CreateJsonRemoteCommandRequest.isParam).toList();
		
		return !list.isEmpty();
	}
	
	private boolean containsUnknownParameter(IInstructionNodeFrame instruction){
		Vertex v = instruction.asVertex();
		GremlinPipeline<Vertex, Vertex>pipe = new GremlinPipeline<Vertex, Vertex>(v);
		List<Vertex> list = pipe.outE(RATConstants.EdgeInstructionParameter).inV().filter(CreateJsonRemoteCommandRequest.isParam).toList();
		
		return !list.isEmpty();
	}
}
