/**
 * @author Daniele Grignani (dgr)
 * @date Aug 28, 2015
 */

package com.dgr.rat.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RATJsonUtils {
	public static String jsonPrettyPrinter(String json) throws Exception{
	    ObjectMapper mapper = new ObjectMapper();
	    Object obj = mapper.readValue(json, Object.class);
	    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	    
	    return indented;
	}
}
