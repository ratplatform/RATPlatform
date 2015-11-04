package com.dgr.rat.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class KeepAliveHelpers {
	
	
	public static String serializeKeepAliveJson(final JsonHeader header){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> headerMap = header.getHeaderProperties();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, "");
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
	public static JsonHeader deserializeKeepAliveJson(final String ratJson) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		TypeFactory typeFactory = mapper.getTypeFactory();
		
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, String.class);
		HashMap<String, String> map = mapper.readValue(jsonHeader.getHeader(), mapType);
		JsonHeader header = new JsonHeader();
		header.setHeaderProperties(map);

		return header;
	}
}
