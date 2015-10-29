package com.dgr.rat.login.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginResponseDeserializer {

    
	public LoginResponseDeserializer() {
		// TODO Auto-generated constructor stub
	}
	
	public LoginResponse deserialize(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    LoginResponse domainSettings = mapper.readValue(json, LoginResponse.class);
	    
	    return domainSettings;
	}
}
