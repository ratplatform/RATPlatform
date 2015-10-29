/**
 * @author Daniele Grignani (dgr)
 * @date Jun 2, 2015
 */

package com.dgr.rat.commons.mqmessages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class MQMessageHelpers {
	public static <T> T deserialize(Class<T> jsonClass, String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    T obj = mapper.readValue(json, jsonClass);
	    
	    return obj;
	}
	
	public static String serialize(Object obj) throws Exception{
		String json = null;
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
		try {
			json = mapper.writeValueAsString(obj);
			//System.out.println(json);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return json;
	}
}
