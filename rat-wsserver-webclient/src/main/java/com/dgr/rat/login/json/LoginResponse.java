/*
 * @author Daniele Grignani
 * Mar 14, 2015
*/

package com.dgr.rat.login.json;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
 
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class LoginResponse {
	private String _sessionID = null;
	private String _userName = null;
	private List<String> _domains = new ArrayList<String>();
	private String _statusResponse = null;
	
    public String get_sessionID() {
		return _sessionID;
	}

    @JsonProperty("sessionID")
    public void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}

	@JsonProperty("userDomains")
	public void setUserDomains(List<String> domains) {
		this._domains = domains;
	}
	
	public String[] getUserDomains(){
		return _domains.toArray(new String[_domains.size()]);
	}
	
	public String getUserName() {
		return _userName;
	}

	@JsonProperty("userName")
	public void setUserName(String userName) {
		this._userName = userName;
	}

	public String getStatusResponse() {
		return _statusResponse;
	}

	@JsonProperty("statusResponse")
	public void setStatusResponse(String statusResponse) {
		this._statusResponse = statusResponse;
	}
}
