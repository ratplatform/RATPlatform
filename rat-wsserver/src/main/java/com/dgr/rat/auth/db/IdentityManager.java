package com.dgr.rat.auth.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
//	public String getUserDomains(String sessionID, String userName){
//		String result = null;
//		//Corretto ma per ora non serve
////		Query query = _entityManager.createNamedQuery("findUserByName");
////		query.setParameter("userName", userName);
////		User user = (User) query.getSingleResult();
//		
//		Query query = _entityManager.createNamedQuery("findDomainsByUserName");
//		query.setParameter("userName", userName);
//		@SuppressWarnings("unchecked")
//		List<UserDomains> userDomains = query.getResultList();
//		List<String> list = new ArrayList<String>();
//		for(UserDomains userDomain : userDomains){
//			list.add(userDomain.get_domainName());
//		}
//		
//		//System.out.println(user.get_email());
//		Map<String, Object> userDomainsMap = new HashMap<String, Object>();;
//		userDomainsMap.put("sessionID", sessionID);
//		userDomainsMap.put("userName", userName);
//		userDomainsMap.put("userDomains", list);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		try{
//			result = objectMapper.writeValueAsString(userDomainsMap);
//			//System.out.println(result);
//		}
//		catch (IOException e){
//			// TODO log e gestione
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
	
//	public String getDomainRoles(String sessionID, String userName, String domainName){
//		String result = null;
//		Query query = _entityManager.createNamedQuery("findRolesByUserNameAndDomainName");
//		query.setParameter("userName", userName);
//		query.setParameter("domainName", domainName);
//		@SuppressWarnings("unchecked")
//		List<DomainRole> domainRoles = query.getResultList();
//		List<String> list = new ArrayList<String>();
//		for(DomainRole domainRole : domainRoles){
//			list.add(domainRole.get_roleName());
//		}
//		
//		Map<String, Object> userDomainsMap = new HashMap<String, Object>();;
//		userDomainsMap.put("sessionID", sessionID);
//		userDomainsMap.put("userName", userName);
//		userDomainsMap.put("domainName", domainName);
//		userDomainsMap.put("domainRoles", list);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		try{
//			result = objectMapper.writeValueAsString(userDomainsMap);
//			System.out.println(result);
//		}
//		catch (IOException e){
//			// TODO log e gestione
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
	
//	public String getDomainRoles(int userID, int domainID){
//		String result = null;
//		Query query = _entityManager.createNamedQuery("findRolesByDomainIDAndUserID");
//		query.setParameter("userID", userID);
//		query.setParameter("domainID", domainID);
//		@SuppressWarnings("unchecked")
//		List<DomainRole> domainRoles = query.getResultList();
//		ObjectMapper objectMapper = new ObjectMapper();
//		try{
//			result = objectMapper.writeValueAsString(domainRoles);
//			System.out.println(result);
//		}
//		catch (IOException e){
//			// TODO log e gestione
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
//
//	public int get_dbUserID() {
//		return _dbUserID;
//	}
//
//	private void set_dbUserID(int dbUserID) {
//		this._dbUserID = dbUserID;
//	}

}
