package com.dgr.rat.auth.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "user_domain")
@NamedQuery(name = "findDomainsByUserName", query="select d from UserDomains d where d._userName = :userName")
public class UserDomains {
	@Id @GeneratedValue
	@Column(name = "userDomainID")
	private int _userDomainID = -1;

	@Column(name = "userName")
	private String _userName = null;
	
	@Column(name = "domainName")
	private String _domainName = null;
	
	@Column(name = "domainUUID")
	private String _domainUUID = null;
	
	public UserDomains() {
		// TODO Auto-generated constructor stub
	}
	
	public String get_domainUUID(){
		return _domainUUID; 
	}
	
	public String get_domainName(){
		return _domainName; 
	}
	
	public String get_userName(){
		return _userName; 
	}

	public int get_userDomainID() {
		return _userDomainID;
	}

}
