package com.dgr.rat.auth.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "domain_role")
@NamedQueries({
	//@NamedQuery(name = "findRolesByDomainIDAndRoleID", query="select dr from DomainRole dr where dr._roleID = :roleID and dr._domainID = :domainID"),
	@NamedQuery(name = "findRolesByUserNameAndDomainName", query="select dr from DomainRole dr where dr._userName = :userName and dr._domainName = :domainName")
})
public class DomainRole {
	@Id @GeneratedValue
	@Column(name = "domainRoleID")
	private int _domainRoleID = -1;
	@Column(name = "domainName")
	private String _domainName = null;
	@Column(name = "userName")
	private String _userName = null;
	@Column(name = "roleName")
	private String _roleName = null;
	
	public DomainRole() {
		// TODO Auto-generated constructor stub
	}
	
	public int get_domainRoleID() {
		return _domainRoleID;
	}
	
	public String get_domainName(){
		return _domainName; 
	}
	
	public String get_userName(){
		return _userName; 
	}
	
	public String get_roleName(){
		return _roleName; 
	}
}
