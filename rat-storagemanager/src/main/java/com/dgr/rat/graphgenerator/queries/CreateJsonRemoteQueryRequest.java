/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.graphgenerator.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.test.RemoteParameter;
import com.dgr.rat.json.utils.ReturnType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public class CreateJsonRemoteQueryRequest {
	private Map<String, RemoteParameter> _parameters = new HashMap<String, RemoteParameter>();
	private List<Vertex>_alreadyExplored = new ArrayList<Vertex>();
	
	public CreateJsonRemoteQueryRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public String makeRemoteRequest(Vertex root) throws Exception{
		this.makeRemoteClientCommand(root);

		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(_parameters);
		
		return output;
	}
	
	public Map<String, RemoteParameter> getParameters(){
		return _parameters;
	}
	
	private void makeRemoteClientCommand(Vertex vertex) throws Exception{
		if(!_alreadyExplored.contains(vertex)){
			_alreadyExplored.add(vertex);
			
			GremlinPipeline<Vertex, Vertex>pipe = new GremlinPipeline<Vertex, Vertex>(vertex);
//			List<Vertex> list = pipe.outE(RATConstants.EdgeInstructionParameter).inV().filter(CreateJsonRemoteQueryRequest.isParam).toList();
			List<Vertex> list = pipe.outE(RATConstants.EdgeInstruction).inV().outE(RATConstants.EdgeInstructionParameter).inV().filter(CreateJsonRemoteQueryRequest.isParam).toList();
			// COMMENT mi aspetto un solo parametro
			if(!list.isEmpty() && list.size() == 1){
				Vertex param = list.get(0);
				RemoteParameter parameter = new RemoteParameter();
				parameter.setInstructionOrder(0);
				parameter.setParameterName(param.getProperty(RATConstants.VertexInstructionParameterNameField).toString());
				parameter.setParameterValue(param.getProperty(RATConstants.VertexInstructionParameterValueField).toString());
				parameter.setReturnType(ReturnType.fromString(param.getProperty(RATConstants.VertexInstructionParameterReturnTypeField).toString()));
				_parameters.put(param.getProperty(RATConstants.VertexUUIDField).toString(), parameter);
			}
			else{
				// COMMENT: faccio tutto 'sto rigiro perché nel caso ci fosse un errore VertexType.fromString lancia un'exception
//				VertexType type = VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString());
//				if(type.toString().equalsIgnoreCase(vertex.getProperty(RATConstants.VertexTypeField).toString())){
//					throw new Exception();
//				}
			}
		}
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
}
