package com.dgr.rat.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class LoginData {
	private String _userName = null;
	private String _password = null;

	public LoginData() {
		// TODO Auto-generated constructor stub
	}
	
    public String getUserName() {
		return _userName;
	}

    @JsonProperty("userName")
    public void setUserName(String userName) {
		this._userName = userName;
	}

    public String getPassword() {
		return _password;
	}

    @JsonProperty("password")
    public void setPassword(String password) {
		this._password = password;
	}

}
