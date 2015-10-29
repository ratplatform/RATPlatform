/**
 * @author Daniele Grignani (dgr)
 * @date Aug 28, 2015
 */

package com.dgr.rat.json.utils;

import com.dgr.rat.commons.constants.RATConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class RATJson {
	private Object _settings = null;
	private Object _header = null;
	
    @JsonRawValue
    @JsonProperty(RATConstants.Settings)
    protected void setSettings(Object settings){
    	_settings = settings;
    }
    
	@JsonRawValue
    @JsonProperty(RATConstants.Header)
    protected void setHeader(Object header){
    	_header = header;
    }
	
    public Object getSettings(){
    	return _settings;
    }
    
    public Object getHeader(){
    	return _header;
    }
}
