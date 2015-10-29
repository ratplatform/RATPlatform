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
public class ChooseDomainResponse {
	private String _sessionID = null;
	private String _userName = null;
	private String _domainName = null;
	private List<String> _roles = new ArrayList<String>();
	private String _statusResponse = null;
	
    public String get_sessionID() {
		return _sessionID;
	}

    @JsonProperty("sessionID")
    public void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}

	@JsonProperty("domainRoles")
	public void setDomainRoles(List<String> roles) {
		this._roles = roles;
	}
	
	public String[] getDomainRoles(){
		return _roles.toArray(new String[_roles.size()]);
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

	public String getDomainName() {
		return _domainName;
	}

	@JsonProperty("domainName")
	public void setDomainName(String domainName) {
		this._domainName = domainName;
	}
}
