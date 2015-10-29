package com.dgr.rat.login.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class ChooseDomainData {
	private String _sessionID = null;
	private String _userName = null;
	private String _domainName = null;
	
	public ChooseDomainData() {
		// TODO Auto-generated constructor stub
	}
	
    @JsonProperty("domainName")
    public void set_domainName(String _domainName) {
		this._domainName = _domainName;
	}
    
    public String get_domainName() {
		return _domainName;
	}
	
    @JsonProperty("sessionID")
    public void set_sessionID(String sessionID) {
		this._sessionID = sessionID;
	}
    
    public String get_sessionID() {
		return _sessionID;
	}

	public String get_userName() {
		return _userName;
	}
	
	@JsonProperty("userName")
	public void set_userName(String userName) {
		this._userName = userName;
	}
}
