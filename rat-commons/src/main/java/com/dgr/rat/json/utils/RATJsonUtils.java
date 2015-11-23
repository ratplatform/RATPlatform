/**
 * @author Daniele Grignani (dgr)
 * @date Aug 28, 2015
 */

package com.dgr.rat.json.utils;

import java.io.IOException;
import java.util.HashMap;

import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.utils.AppProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class RATJsonUtils {
	public static String jsonPrettyPrinter(String json) throws Exception{
	    ObjectMapper mapper = new ObjectMapper();
	    Object obj = mapper.readValue(json, Object.class);
	    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	    
	    return indented;
	}
	
	public static JsonHeader deserializeJsonHeader(final String ratJson) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject ratJsonObject = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		
		TypeFactory typeFactory = mapper.getTypeFactory();
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, String.class);
		HashMap<String, String> map = mapper.readValue(ratJsonObject.getHeader(), mapType);
		JsonHeader header = new JsonHeader();
		header.setHeaderProperties(map);

		return header;
	}
	
	public static JsonHeader getJsonHeader(StatusCode commandResult, MessageType messageType){
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		JsonHeader header = new JsonHeader();
		
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setDomainName(placeHolder);
		header.setMessageType(messageType);
		header.setStatusCode(commandResult);
		
		return header;
	}
	
	public static String getSettings(RATJsonObject ratJson) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String settings = mapper.writeValueAsString(ratJson.getSettings());
		
		return settings;
	}
	
	public static String getHeader(RATJsonObject ratJson) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String settings = mapper.writeValueAsString(ratJson.getHeader());
		
		return settings;
	}
	
	public static String getRATJson(RATJsonObject ratJson) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(ratJson);
		
		return json;
	}
	

	public static String getRATJsonHeaderProperty(String ratJson, String propertyName) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		String propertyValue = jsonHeader.getHeaderProperty(propertyName);
		
		return propertyValue;
	}
	
//	public static String getResponseJsonProperty(String responseJson, String propertyName) throws JsonParseException, JsonMappingException, IOException{
//		ObjectMapper mapper = new ObjectMapper();
//		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(responseJson, RATJsonObject.class);
//		String propertyValue = jsonHeader.getHeaderProperty(propertyName);
//		
//		return propertyValue;
//	}
	
	public static String getRATJsonSettings(String ratJson) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		String output = mapper.writeValueAsString(jsonHeader.getSettings());
		
		return output;
	}
	

	
	public static RATJsonObject getRATJsonObject(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject ratJsonObject = (RATJsonObject) mapper.readValue(json, RATJsonObject.class);
		
		return ratJsonObject;
	}
	
	public static String getRATJsonObjectHeader(String ratJson) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		String output = mapper.writeValueAsString(jsonHeader.getHeader());
		
		return output;
	}
}
