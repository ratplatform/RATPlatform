package com.dgr.rat.auth.db.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_domain")
@IdClass(UserDomainsID.class)
public class UserDomains {
	@Id
	@Column(name = "userID")
	private int _userID = -1;
	@Id
	@Column(name = "domainID")
	private int _domainID = -1;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "domainID", nullable = false)
	private Domain _domain = null;
	
	public UserDomains() {
		// TODO Auto-generated constructor stub
	}
	
	public Domain get_domain(){
		return _domain; 
	}
	
	public String get_domainName(){
		return _domain.get_domainName(); 
	}
	
	public int get_userID() {
		return _userID;
	}

	public int get_domainID() {
		return _domainID;
	}

}
