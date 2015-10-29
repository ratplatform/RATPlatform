package com.dgr.rat.auth.db.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "domain")
public class Domain {
	@Id
	@Column(name = "domainID")
	private int _domainID = -1;
	@Column(name = "domainName")
	private String _name = null;
	
	public Domain() {
		// TODO Auto-generated constructor stub
	}

	public String get_domainName() {
		return _name;
	}
	
	public int get_domainID() {
		return _domainID;
	}
}
