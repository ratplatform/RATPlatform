/*
 * @author Daniele Grignani
 * Mar 14, 2015
*/

package com.dgr.rat.login.json;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
 
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class LoginResponse {
	private String _sessionID = null;
	private String _userUIID = null;
	private String _userName = null;
	private Map<String, String> _userDomains = new HashMap<String, String>();
	private Object _statusCode = null;
	
    public String get_sessionID() {
		return _sessionID;
	}

    @JsonProperty("sessionID")
    public void set_sessionID(String sessionID) {
		this._sessionID = sessionID;
	}

	@JsonProperty("userDomains")
	public void set_userDomains(Map<String, String> userDomains) {
		this._userDomains = userDomains;
	}
	
	public Map<String, String> getUserDomains(){
		return _userDomains;
	}
	
	public String getUserName() {
		return _userName;
	}

	@JsonProperty("userName")
	public void set_userName(String userName) {
		this._userName = userName;
	}

	public Object getStatusCode() {
		return _statusCode;
	}

	@JsonProperty("StatusCode")
	public void set_statusCode(Object statusCode) {
		this._statusCode = statusCode;
	}

	public String get_userUIID() {
		return _userUIID;
	}
	
	@JsonProperty("userUIID")
	public void set_userUIID(String userUIID) {
		this._userUIID = userUIID;
	}
}
