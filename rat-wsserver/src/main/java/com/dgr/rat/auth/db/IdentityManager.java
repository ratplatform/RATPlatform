package com.dgr.rat.auth.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dgr.rat.webservices.RATWebServicesContextListener;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IdentityManager {
	private EntityManager _entityManager = RATWebServicesContextListener.createEntityManager(); 
	private int _dbUserID = -1;
	
	public IdentityManager() {
		// TODO Auto-generated constructor stub
	}
	
	public String userLogin(String email, String password){
		Query query = _entityManager.createNamedQuery("findUserByEmail");
		query.setParameter("email", email);
		Object obj = query.getSingleResult();
		if(obj == null){
			// TODO: log + exception
		}
		User user = (User) obj;
		if(!user.get_password().equals(password)){
			return null;
		}
		
		return UUID.randomUUID().toString();
	}
	
	public Map<String, Object> getUserDomains(String sessionID, String email) throws JsonProcessingException{
		Map<String, Object> result = new HashMap<String, Object>();
		Query query = _entityManager.createNamedQuery("findUserByEmail");
		query.setParameter("email", email);
		// TODO: in realtà dovrei farmi restituire una lista e verificare il numero degli elementi e lanciare un'exception se è != 1
		Object obj = query.getSingleResult();
		if(obj == null){
			// TODO: log + exception
		}
		User user = (User) obj;
		
		String uuid = user.get_userUUID();
		if(!Utils.isUUID(uuid)){
			// TODO: log + exception
		}
		
		query = _entityManager.createNamedQuery("findDomainsByUserUUID");
		query.setParameter("userUUID", uuid);
		@SuppressWarnings("unchecked")
		List<UserDomains> userDomains = query.getResultList();
		
		Map<String, String> map = new HashMap<String, String>();
		for(UserDomains userDomain : userDomains){
			map.put(userDomain.get_domainName(), userDomain.get_domainUUID());
		}

		//System.out.println(user.get_email());
		result.put("sessionID", sessionID);
		result.put("email", email);
		result.put("userDomains", map);
		result.put("userUUID", uuid);
		
		return result;
	}
	
	public Map<String, Object> getDomainRoles(String sessionID, String userName, String domainName){
		Map<String, Object> result = new HashMap<String, Object>();
		Query query = _entityManager.createNamedQuery("findRolesByUserNameAndDomainName");
		query.setParameter("userName", userName);
		query.setParameter("domainName", domainName);
		@SuppressWarnings("unchecked")
		List<DomainRole> domainRoles = query.getResultList();
		List<String> list = new ArrayList<String>();
		for(DomainRole domainRole : domainRoles){
			list.add(domainRole.get_roleName());
		}
		
		result.put("sessionID", sessionID);
		result.put("userName", userName);
		result.put("domainName", domainName);
		result.put("domainRoles", list);
		
		return result;
	}
}
