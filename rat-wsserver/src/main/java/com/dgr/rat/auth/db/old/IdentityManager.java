package com.dgr.rat.auth.db.old;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dgr.rat.webservices.RATWebServicesContextListener;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IdentityManager {
	private EntityManager _entityManager = RATWebServicesContextListener.createEntityManager(); 
	private int _dbUserID = -1;
	
	public IdentityManager() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUser(String sessionID, String userName){
		String result = null;
		Query query = _entityManager.createNamedQuery("findUserByName");
		query.setParameter("userName", userName);
		User user = (User) query.getSingleResult();
		this.set_dbUserID(user.get_userID());
		//System.out.println(user.get_email());
		Map<String, String> userDomainsMap = user.getUserDomains();
		userDomainsMap.put("sessionID", sessionID);
		
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			result = objectMapper.writeValueAsString(userDomainsMap);
			System.out.println(result);
		}
		catch (IOException e){
			// TODO log e gestione
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getDomainRoles(int userID, int domainID){
		String result = null;
		Query query = _entityManager.createNamedQuery("findRolesByDomainIDAndUserID");
		query.setParameter("userID", userID);
		query.setParameter("domainID", domainID);
		@SuppressWarnings("unchecked")
		List<DomainRole> domainRoles = query.getResultList();
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			result = objectMapper.writeValueAsString(domainRoles);
			System.out.println(result);
		}
		catch (IOException e){
			// TODO log e gestione
			e.printStackTrace();
		}
		
		return result;
	}

	public int get_dbUserID() {
		return _dbUserID;
	}

	private void set_dbUserID(int dbUserID) {
		this._dbUserID = dbUserID;
	}

}
