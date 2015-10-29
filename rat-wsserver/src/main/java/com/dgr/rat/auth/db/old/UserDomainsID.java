package com.dgr.rat.auth.db.old;

import java.io.Serializable;

public class UserDomainsID implements Serializable{
	private static final long serialVersionUID = 1L;
	private int _userID = -1;
	private int _domainID = -1;
	
	public UserDomainsID() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode(){
		return super.hashCode();
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof UserDomainsID)) return false;
        
        UserDomainsID userDomainsID = (UserDomainsID) obj;
        return userDomainsID.get_userID() == this.get_userID() && userDomainsID.get_domainID() == this.get_domainID();
    }

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int userID) {
		this._userID = userID;
	}

	public int get_domainID() {
		return _domainID;
	}

	public void set_domainID(int domainID) {
		this._domainID = domainID;
	}
}
