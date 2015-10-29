package com.dgr.rat.session.manager;

public class SessionData {
	private String _userName = null;
	private String _password = null;
	private String _sessionID = null;
	private int _dbUserID = -1;
	
	public SessionData(String sessionID) {
		this.setSessionID(sessionID);
	}
	
	public int get_dbUserID() {
		return _dbUserID;
	}

	public void set_dbUserID(int dbUserID) {
		this._dbUserID = dbUserID;
	}
	
    public String getUserName() {
		return _userName;
	}

    public void setUserName(String userName) {
		this._userName = userName;
	}

    public String getPassword() {
		return _password;
	}

    public void setPassword(String password) {
		this._password = password;
	}

	public String getSessionID() {
		return _sessionID;
	}

	private void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}

}
