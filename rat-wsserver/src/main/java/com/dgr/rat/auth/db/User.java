package com.dgr.rat.auth.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicUpdate;

import com.dgr.rat.auth.visitor.Visitable;
import com.dgr.rat.auth.visitor.Visitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@NamedQuery(name = "findUserByEmail", query="select u from User u where u._email = :email")
public class User implements Visitable{
	@Id @GeneratedValue
	@Column(name = "userID")
	private int _userID = -1;
	@Column(name = "userName")
	private String _userName = null;
	@Column(name = "email")
	private String _email = null;
	@Column(name = "password")
	private String _password = null;
	@Column(name = "userUUID")
	private String _userUUID = null;

	public User() {
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public int get_userID() {
		return _userID;
	}

	public String get_userName() {
		return _userName;
	}

	public String get_email() {
		return _email;
	}

	public String get_password() {
		return _password;
	}

	public String get_userUUID() {
		return _userUUID;
	}

	public void set_userUUID(String userUUID) {
		this._userUUID = userUUID;
	}
}
