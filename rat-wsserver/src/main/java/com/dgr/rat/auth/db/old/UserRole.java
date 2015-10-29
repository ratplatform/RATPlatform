package com.dgr.rat.auth.db.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//@DynamicUpdate //@Entity
@Entity
//@NamedQuery(name = "findRolesFromUserID", query="select * from user_role where userID = :userID")
@Table(name = "user_role")
@IdClass(UserRoleID.class)
public class UserRole {
	@Id
	@Column(name = "userID")
	private int _userID = -1;
	@Id
	@Column(name = "roleID")
	private int _roleID = -1;
	
	//@OneToOne (fetch = FetchType.LAZY)
	//@JoinColumn(name = "userID", nullable = false)
	//private User _user = null;
//	@JoinTable(name = "role",
//	joinColumns = @JoinColumn(name = "roleID"),
//	inverseJoinColumns = @JoinColumn(name = "roleID"))
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "roleID", nullable = false)
	private Role _role = null;
	//private List<Role> _roles = null;
	
	public UserRole() {
		// TODO Auto-generated constructor stub
	}
	
	public Role get_role(){
		return _role; 
	}
	
	public String get_roleName(){
		return _role.get_name(); 
	}

	public int get_userID() {
		return _userID;
	}

	public int get_roleID() {
		return _roleID;
	}

}
