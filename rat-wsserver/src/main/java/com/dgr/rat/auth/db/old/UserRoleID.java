package com.dgr.rat.auth.db.old;

import java.io.Serializable;

public class UserRoleID implements Serializable{
	private static final long serialVersionUID = 1L;
	private int _userID = -1;
	private int _roleID = -1;
	
	public UserRoleID() {
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
        if (!(obj instanceof UserRoleID)) return false;
        
        UserRoleID userRoleID = (UserRoleID) obj;
        return userRoleID.get_userID() == this.get_userID() && userRoleID.get_roleID() == this.get_roleID();
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
}
