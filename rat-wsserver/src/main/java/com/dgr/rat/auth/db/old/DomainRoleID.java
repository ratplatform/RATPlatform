package com.dgr.rat.auth.db.old;

import java.io.Serializable;

public class DomainRoleID implements Serializable{
	private static final long serialVersionUID = 1L;
	private int _userID = -1;
	private int _roleID = -1;
	private int _domainID = -1;
	
	public DomainRoleID() {
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
        if (!(obj instanceof DomainRoleID)) return false;
        
        DomainRoleID domainRoleID = (DomainRoleID) obj;
        return domainRoleID.get_userID() == this.get_userID() && domainRoleID.get_roleID() == this.get_roleID() && domainRoleID.get_domainID() == this.get_domainID();
    }

	public int get_userID() {
		return _userID;
	}

	public void set_userID(int userID) {
		this._userID = userID;
	}

	public int get_roleID() {
		return _roleID;
	}

	public void set_roleID(int roleID) {
		this._roleID = roleID;
	}
	
	public int get_domainID() {
		return _domainID;
	}

	public void set_domainID(int domainID) {
		this._domainID = domainID;
	}
}
