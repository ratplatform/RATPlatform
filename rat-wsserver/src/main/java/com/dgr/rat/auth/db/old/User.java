package com.dgr.rat.auth.db.old;

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
@NamedQuery(name = "findUserByName", query="select u from User u where u._userName = :userName")
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
	
	@OneToMany
	@JoinTable(name = "user_role",
	joinColumns = @JoinColumn(name = "userID"),
	inverseJoinColumns = @JoinColumn(name = "roleID"))
	private List<UserRole> _userRoles = null;
	
	@OneToMany
	@JoinTable(name = "user_domain",
	joinColumns = @JoinColumn(name = "userID"),
	inverseJoinColumns = @JoinColumn(name = "domainID"))
	private List<UserDomains> _userDomains = null;

	public User() {
		//this.setDomains();
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	public Map<String, String> getUserDomains(){
		Map<String, String>map = new HashMap<String, String>();
		if(_userDomains != null){
			System.out.println("this.get_userID: " + this.get_userID());
			for(UserDomains userDomain : _userDomains){
				// TODO: sarebbe meglio fare un controllo sullo String.valueOf
				map.put(userDomain.get_domainName(), String.valueOf(userDomain.get_domainID()));
//				System.out.println("userDomain.get_userID: " + userDomain.get_userID());
//				System.out.println("userDomain.get_domainID: " + userDomain.get_domainID());
//				System.out.println("userDomain.get_domainID: " + userDomain.get_domainName());
			}
		}
		
		return map;
	}
	
	public Map<Integer, String> get_userRoles(){
		Map<Integer, String>map = new HashMap<Integer, String>();
		if(_userRoles != null){
			for(UserRole userRole : _userRoles){
				map.put(userRole.get_roleID(), userRole.get_roleName());
//				System.out.println("userRole.get_role().get_roleID(): " + userRole.get_role().get_roleID());
//				System.out.println("userRole.get_role().get_name(): " + userRole.get_role().get_name());
//				System.out.println("get_userID: " + userRole.get_userID());
//				System.out.println("get_roleID: " + userRole.get_roleID());
//				System.out.println("roleName: " + roleName);
			}
		}
		return map;
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
}
