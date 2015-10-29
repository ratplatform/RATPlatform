package com.dgr.rat.auth.db.old;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@NamedQuery(name = "findRolesByDomainIDAndRoleID", query="select dr from DomainRole dr where dr._roleID = :roleID and dr._domainID = :domainID"),
	@NamedQuery(name = "findRolesByDomainIDAndUserID", query="select dr from DomainRole dr where dr._userID = :userID and dr._domainID = :domainID")
})
@IdClass(DomainRoleID.class)
public class DomainRole {
	@Id
	@Column(name = "domainID")
	private int _domainID = -1;
	@Id
	@Column(name = "roleID")
	private int _roleID = -1;
	@Id
	@Column(name = "userID")
	private int _userID = -1;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "roleID", nullable = false)
	private Role _role = null;
	
	public DomainRole() {
		// TODO Auto-generated constructor stub
	}
	
//	public Map<String, String> get_userRoles(){
//		Map<String, String>map = new HashMap<String, String>();
//		if(_role != null){
////			Iterator<String> it = map.keySet().iterator();
////			while(it.hasNext()){
////				String key 
////			}
//		}
//		return map;
//	}
	
	public String getUserRoleName(){
		String result = "";
		if(_role != null){
			result = _role.get_name();
		}
		return result;
	}
	
	public int getDomainID() {
		return _domainID;
	}

	public int getRoleID() {
		return _roleID;
	}
	
	public int getUserID() {
		return _userID;
	}
}
