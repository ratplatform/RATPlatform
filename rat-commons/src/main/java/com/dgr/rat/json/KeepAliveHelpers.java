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
}
