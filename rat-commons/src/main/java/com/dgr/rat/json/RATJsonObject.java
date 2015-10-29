/**
 * @author Daniele Grignani (dgr)
 * @date Jul 15, 2015
 */

package com.dgr.rat.json;

import java.util.HashMap;
import java.util.Map;
import com.dgr.rat.commons.constants.RATConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RATJsonObject implements IRATJsonObject{
	private Map<String, String> _headerProperties = new HashMap<String , String>();
    private Object _settings = null;
    
	public RATJsonObject() {
//		System.out.println("JSONHeader");
	}
    
    @SuppressWarnings("unchecked")
	@JsonRawValue
    @JsonProperty(RATConstants.Header)
    public void setHeader(Object header){
		try {
			if(header instanceof Map){
				_headerProperties = (Map<String, String>) header;
			}
			else{
				// TODO: da inserire exception e gestire in qualche modo
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String getHeader() throws JsonProcessingException{
    	ObjectMapper objectMapper = new ObjectMapper();
    	String header = objectMapper.writeValueAsString(_headerProperties);
    	
    	return header;
    }
    
    public String getHeaderProperty(String property){
    	String result = null;
    	if(_headerProperties.containsKey(property)){
    		result = _headerProperties.get(property);
    	}
    	return result;
    }

    @JsonRawValue
    @JsonProperty(RATConstants.Settings)
    public void setSettings(Object settings){
    	_settings = settings;
    }
    
    public Object getSettings(){
    	Object result = null;
    	if(_settings != null){
    		result = _settings;
    	}
    	
    	return result;
    }
}
