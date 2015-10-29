/*
 * @author Daniele Grignani
 * Mar 14, 2015
*/

package com.dgr.rat.login.json;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
 
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class LoginResponse {
	private Map<String , String> _domains = new HashMap<String , String>();
	private String _sessionID = null;

    public String get_sessionID() {
		return _sessionID;
	}

    @JsonProperty("sessionID")
    public void set_sessionID(String sessionID) {
		this._sessionID = sessionID;
	}
    
    @JsonAnyGetter
    public Map<String , String> any() {
        return _domains;
    }
    
    @JsonAnySetter
    public void set(String name, Object value) {
    	//System.out.println("AlbumsFilter: " + name);
    	_domains.put(name, value.toString());
    }
}
