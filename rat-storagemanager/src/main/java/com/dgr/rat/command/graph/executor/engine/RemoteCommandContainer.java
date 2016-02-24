/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import com.dgr.rat.json.command.parameters.RemoteParameter;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	public String getParameter(final IInstructionParam instructionParameter) throws Exception{
		String paramUUID = instructionParameter.getParamUUID();
		if(!Utils.isUUID(paramUUID)){
			throw new Exception();
			// TODO log
		}
		
		String paramName = instructionParameter.getInstructionsParameterNameField();
		if(!_parameters.containsKey(paramName)){
			throw new Exception();
			// TODO log
		}
		
		RemoteParameter remoteParameter = _parameters.get(paramName);
		if(!paramUUID.equalsIgnoreCase(remoteParameter.getVertexUUIDField())){
			throw new Exception();
		}
		
		ReturnType remoteParameterReturnType = remoteParameter.getReturnType();
		ReturnType type = instructionParameter.getInstructionsParameterReturnTypeField();
		if(!type.toString().equalsIgnoreCase(remoteParameterReturnType.toString())){
			throw new Exception();
			// TODO log
		}
		
		String remoteParameterName = remoteParameter.getParameterName();
		if(!paramName.toString().equalsIgnoreCase(remoteParameterName)){
			throw new Exception();
			// TODO log
		}
		
		String remoteParamValue = remoteParameter.getParameterValue();
		
		return remoteParamValue;
	}
}
