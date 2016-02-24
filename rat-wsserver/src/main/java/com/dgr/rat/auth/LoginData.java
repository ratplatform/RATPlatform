package com.dgr.rat.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class LoginData {
	private String _email = null;
	private String _password = null;
	private String _query = null;		// TODO Auto-generated constructor stub
	
	
    public String get_email() {
		return _email;
	}

    @JsonProperty("email")
    public void set_email(String email) {
		this._email = email;
	}

    public String getPassword() {
		return _password;
	}

    @JsonProperty("password")
    public void setPassword(String password) {
		this._password = password;
	}
    
    @JsonProperty("query")
    public void setQuery(String query) {
		this._query = query;
	}
    
    public String getQuery() {
		return this._query;
	}

}
