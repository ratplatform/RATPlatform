package com.dgr.rat.auth.db.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@Column(name = "roleID")
	private int _roleID = -1;
	@Column(name = "name")
	private String _name = null;
	
	public Role() {
		// TODO Auto-generated constructor stub
	}

	public int get_roleID() {
		return _roleID;
	}

	public String get_name() {
		return _name;
	}
}
