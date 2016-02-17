/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.json.command.parameters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import com.dgr.rat.json.utils.ReturnType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// COMMENT: attenzione è un dosspione della classe RemoteCommandContainer in com.dgr.rat.command.graph.executor.engine
// ed è stata duplicata qui solo per comodità in quanto viene usata da SystemInitializerTestHelpers

public class RemoteCommandContainer{
	private Map<String, RemoteParameter> _parameters = new HashMap<String, RemoteParameter>();
	
	public RemoteCommandContainer() {
		// TODO Auto-generated constructor stub
	}
	
	public void setValue(UUID paramUUID, String value, ReturnType type) throws Exception{
		RemoteParameter remoteParameter = _parameters.get(paramUUID.toString());
		if(remoteParameter == null){
			throw new Exception();
			// TODO: log?
		}
		
		ReturnType remoteParameterType = remoteParameter.getReturnType();
		if(!remoteParameterType.toString().equalsIgnoreCase(type.toString())){
			throw new Exception();
			// TODO: log?
		}
		
		remoteParameter.setParameterValue(value);
	}
	
	public int setValue(String paramName, String value, ReturnType type) throws Exception{
		Iterator<Entry<String, RemoteParameter>>it = _parameters.entrySet().iterator();
		int result = 0;
		while(it.hasNext()){
			Entry<String, RemoteParameter> entry = it.next();
			String key = entry.getValue().getParameterName();
			ReturnType remoteParameterType = entry.getValue().getReturnType();
			
			if(key.equalsIgnoreCase(paramName) && remoteParameterType.toString().equalsIgnoreCase(type.toString())){
				entry.getValue().setParameterValue(value);
				result++;
			}
		}
		
		return result;
	}

	public void deserialize(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<Map<String, RemoteParameter>> typeRef = new TypeReference<Map<String, RemoteParameter>>() {};
		_parameters = mapper.readValue(json, typeRef);
	}
	
	public String serialize() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(_parameters);
		
		return output;
	}
}
