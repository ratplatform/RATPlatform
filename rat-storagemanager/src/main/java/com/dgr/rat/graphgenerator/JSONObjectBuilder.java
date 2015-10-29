/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.graphgenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.graphgenerator.queries.CreateJsonRemoteQueryRequest;
import com.dgr.rat.graphgenerator.test.CreateJsonRemoteCommandRequest;
import com.dgr.rat.json.factory.JsonHeader;
import com.dgr.rat.json.factory.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class JSONObjectBuilder {
	public static final String Header = "header";
	public static final String Settings = "settings";
	
	public static String buildJSONRatCommandResponse(Response response){
		JsonHeader header = response.getHeader();
		Map<String, String> headerMap = header.getHeaderProperties();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		Map<String, Object>map = null;
		Map<String, Object> commandResponsePropertiesMap = response.getCommandResponse().getResult();
		if(commandResponsePropertiesMap.containsKey(JSONObjectBuilder.Settings)){
			map = commandResponsePropertiesMap;
		}
		else{
			map = new HashMap<String, Object>();
			map.put(JSONObjectBuilder.Settings, commandResponsePropertiesMap);
		}
		map.put(JSONObjectBuilder.Header, headerObjectNode);

		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
//	public static String buildJSONRatCommandResponse(Response response){
//		JsonHeader header = response.getHeader();
//		Map<String, String> headerMap = header.getHeaderProperties();
//
//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
//		
//		IInstructionResult commandResponse = response.getResponse();
//		Map<String, Object> commandResponsePropertiesMap = commandResponse.getCommandResponseProperties();
//		ObjectNode commandResponsePropertieObjectNode = mapper.valueToTree(commandResponsePropertiesMap);
//		
//		Map<String, Object>map = new HashMap<String, Object>();
//		map.put(JSONObjectBuilder.Header, headerObjectNode);
//		map.put(JSONObjectBuilder.Settings, commandResponsePropertieObjectNode);
//		
//		ObjectNode ratJsonObject = mapper.valueToTree(map);
//		String result = ratJsonObject.toString();
//		
//		return result;
//	}
	
	public static String buildRemoteCommand(final JsonHeader header, final IRATNodeFrame node) throws Exception{
		Map<String, String> headerMap = header.getHeaderProperties();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		CreateJsonRemoteCommandRequest jsonCommand = new CreateJsonRemoteCommandRequest();
		String remoteRequestJson = jsonCommand.makeRemoteRequest(node);
		JsonNode remoteRequestJsonObj = mapper.readTree(remoteRequestJson);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(JSONObjectBuilder.Header, headerObjectNode);
		map.put(JSONObjectBuilder.Settings, remoteRequestJsonObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
	public static String buildRemoteQuery(final JsonHeader header, final Vertex node) throws Exception{
		Map<String, String> headerMap = header.getHeaderProperties();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		CreateJsonRemoteQueryRequest jsonCommand = new CreateJsonRemoteQueryRequest();
		String remoteRequestJson = jsonCommand.makeRemoteRequest(node);
		JsonNode remoteRequestJsonObj = mapper.readTree(remoteRequestJson);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(JSONObjectBuilder.Header, headerObjectNode);
		map.put(JSONObjectBuilder.Settings, remoteRequestJsonObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
	public static String buildCommandTemplate(final JsonHeader header, Graph commandTemplateGraph) throws IOException{
	    OutputStream output = new OutputStream(){
	        private StringBuilder string = new StringBuilder();
	        
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    
		GraphSONWriter.outputGraph(commandTemplateGraph, output);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(output.toString());
		
		Map<String, String> headerMap = header.getHeaderProperties();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(JSONObjectBuilder.Header, headerObjectNode);
		map.put(JSONObjectBuilder.Settings, actualObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
	    
	    return result;
	}
}
