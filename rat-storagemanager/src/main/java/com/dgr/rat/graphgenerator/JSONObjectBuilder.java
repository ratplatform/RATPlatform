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
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.IResponse;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.graphgenerator.queries.CreateJsonRemoteQueryRequest;
import com.dgr.rat.graphgenerator.test.CreateJsonRemoteCommandRequest;
import com.dgr.rat.json.RATJsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class JSONObjectBuilder {
	
	public static String serializeCommandResponse(IResponse response){
		JsonHeader header = response.getHeader();
		Map<String, String> headerMap = header.getHeaderProperties();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		Map<String, Object>map = null;
		Map<String, Object> commandResponsePropertiesMap = response.getResult();
		if(commandResponsePropertiesMap.containsKey(RATConstants.Settings)){
			map = commandResponsePropertiesMap;
		}
		else{
			map = new HashMap<String, Object>();
			map.put(RATConstants.Settings, commandResponsePropertiesMap);
		}
		map.put(RATConstants.Header, headerObjectNode);

		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
	public static MQMessage deserializeCommandResponse(String ratJson) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		TypeFactory typeFactory = mapper.getTypeFactory();
		
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, String.class);
		HashMap<String, String> map = mapper.readValue(jsonHeader.getHeader(), mapType);
		JsonHeader header = new JsonHeader();
		header.setHeaderProperties(map);
		
		mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
		String settings = mapper.writeValueAsString(jsonHeader.getSettings());
//		System.out.println(settings);
		HashMap<String, Object> map2 = mapper.readValue(settings, mapType);
		MQMessage message = new MQMessage(header);
		message.setCommandResponse(map2);
		
		return message;
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
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, remoteRequestJsonObj);
		
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
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, remoteRequestJsonObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
//	public static String buildJavaScript(final JsonHeader header, final String remoteRequestJson) throws Exception{
//		RATJsonObject ratJsonObject = RATJsonUtils.getRATJsonObject(remoteRequestJson);
//		BuildQueryJavaScript buildQueryJavaScript = new BuildQueryJavaScript();
//		buildQueryJavaScript.make(ratJsonObject);
//		if(buildQueryJavaScript.getHeader() == null){
//			buildQueryJavaScript.setHeader(header);
//		}
//		return null;
//	}
	
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
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, actualObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
	    
	    return result;
	}
}
