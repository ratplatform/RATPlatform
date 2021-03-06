/*
 * @author Daniele Grignani
 * Mar 18, 2015
*/

package com.dgr.rat.webservices;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.dgr.rat.auth.LoginData;
import com.dgr.rat.auth.db.IdentityManager;
import com.dgr.rat.auth.db.IncorrectCredentialsException;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.messages.IMessageSender;
import com.dgr.rat.messages.RATMessageSender;
import com.dgr.rat.session.manager.RATSessionManager;
import com.dgr.rat.session.manager.SessionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/rat")
@Stateless
public class RATWebServices {
	public static final String CURRENT_USER_KEY = "user";
	public static final String CURRENT_USER_PWD = "pwd";
//	public static final String STATUS_RESPONSE = "statusResponse";
	//@PersistenceContext(unitName="ratwsserver")
//	private EntityManager _entityManager = RATWebServicesContextListener.createEntityManager(); 
	@Context
	private ServletContext _context; 
	@Context 
	private HttpServletRequest _request;
	
	//@Inject
	//private IMessageSender _ratMessageSender;
	
	public RATWebServices() {
		System.out.println("RATWebServices");
	}
	
	@POST @Path("/v0.1/logout")
	@Produces(MediaType.TEXT_PLAIN)
	public Response logout(@QueryParam("sessionid") String sessionID){
		int responseStatus = 200;
		Response response = null;
		Map<String, Object> result = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
		try {
			if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
				responseStatus = 401;
				throw new Exception("sessionID is empty");
			}
			
			if(!RATSessionManager.getInstance().sessionIDExists(sessionID)){
				responseStatus = 401;
				throw new Exception("sessionID does not exist");
			}
			
			if(!RATSessionManager.getInstance().killSession(sessionID)){
				responseStatus = 401;
				throw new Exception("sessionID does not exist");
			}
		}
		catch (Exception e) {
			// TODO log
			e.printStackTrace();
			if(responseStatus == 200){
				responseStatus = 500;
			}
		}
		
    	String json = null;
        result.put(RATConstants.StatusCode, responseStatus);
        
		try {
			json = mapper.writeValueAsString(result);
			System.out.println(json);
		} 
		catch (Exception e) {
			// TODO: gestire l'errore creando un JSON con errore e statuscode 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	response = Response.status(responseStatus).entity(json).build();
    	return response;
	}
	
	// TODO: @Produces(MediaType.APPLICATION_JSON)
	// TODO: @Consumes(MediaType.APPLICATION_JSON)
	@POST @Path("/v0.1/login")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes (MediaType.TEXT_PLAIN)
	public Response login(String data){
        System.out.println("login");
        System.out.println(data);
        
        int responseStatus = 200;
        Response response = null;
        Map<String, Object> result = null;
        ObjectMapper mapper = null;
        String sessionID = null;
        LoginData authenticationData = null;
        try {
    		mapper = new ObjectMapper();
    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	    authenticationData = mapper.readValue(data, LoginData.class);
    	    
    	    IdentityManager identityManager = new IdentityManager();
    	    
    		String login = authenticationData.get_email();
    		String password = authenticationData.getPassword();
    		
    		sessionID = identityManager.userLogin(login, password);
            System.out.println(sessionID);
            if(sessionID == null){
            	throw new IncorrectCredentialsException();
            }
            
            SessionData sessionData = new SessionData(sessionID);
            sessionData.setPassword(password);
            sessionData.setUserName(login);

			result = identityManager.getUserDomains(sessionID, login);
			responseStatus = 200;
			
			RATSessionManager.getInstance().setSessionData(sessionData);
		} 
//        catch (UnknownAccountException e) {
//            System.out.println("Incorrect username/password!");
//            responseStatus = 401;
//            result = new HashMap<String, Object>();
//        }
        catch (IncorrectCredentialsException ice) {
            System.out.println("Incorrect username/password!");
            responseStatus = 401;
            result = new HashMap<String, Object>();
        }
        catch (Exception e) {
			// TODO log
			e.printStackTrace();
			responseStatus = 500;
            result = new HashMap<String, Object>();
		}

    	String json = null;
    	
        result.put(RATConstants.StatusCode, responseStatus);
        if(mapper == null){
        	mapper = new ObjectMapper();
        	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        
		try {
			json = mapper.writeValueAsString(result);
			System.out.println(json);
		} 
		catch (Exception e) {
			// TODO: gestire l'errore creando un JSON con errore e statuscode 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	response = Response.status(responseStatus).entity(json).build();
        
        return response;
	}
//	@POST @Path("/v0.1/login")
//	@Produces(MediaType.TEXT_PLAIN)
//	@Consumes (MediaType.TEXT_PLAIN)
//	public Response login(String data){
//        System.out.println("login");
//        System.out.println(data);
//        
//        int responseStatus = 200;
//        Response response = null;
//        Map<String, Object> result = null;
//        ObjectMapper mapper = null;
//        String sessionID = null;
//        LoginData authenticationData = null;
//        try {
//    		mapper = new ObjectMapper();
//    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//    	    authenticationData = mapper.readValue(data, LoginData.class);
//    	    
//    		String login = authenticationData.get_email();
//    		String password = authenticationData.getPassword();
//            UsernamePasswordToken token = new UsernamePasswordToken(login, password);
//            Subject currentUser = SecurityUtils.getSubject();
//
//            token.setRememberMe(true);
//            currentUser.login(token); 
//
//            sessionID = currentUser.getSession().getId().toString();
//            System.out.println(sessionID);
//            
//            SessionData sessionData = new SessionData(sessionID);
//            sessionData.setPassword(password);
//            sessionData.setUserName(login);
//			
//			IdentityManager identityManager = new IdentityManager();
//			result = identityManager.getUserDomains(sessionID, login);
//			responseStatus = 200;
//			
//			RATSessionManager.getInstance().setSessionData(sessionData);
//		} 
//        catch (UnknownAccountException e) {
//            System.out.println("Incorrect username/password!");
//            responseStatus = 401;
//            result = new HashMap<String, Object>();
//        }
//        catch (IncorrectCredentialsException ice) {
//            System.out.println("Incorrect username/password!");
//            responseStatus = 401;
//            result = new HashMap<String, Object>();
//        }
//        catch (Exception e) {
//			// TODO log
//			e.printStackTrace();
//			responseStatus = 500;
//            result = new HashMap<String, Object>();
//		}
//
//    	String json = null;
//    	
//        result.put(RATConstants.StatusCode, responseStatus);
//        if(mapper == null){
//        	mapper = new ObjectMapper();
//        	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        }
//        
//		try {
//			json = mapper.writeValueAsString(result);
//			System.out.println(json);
//		} 
//		catch (Exception e) {
//			// TODO: gestire l'errore creando un JSON con errore e statuscode 
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//    	response = Response.status(responseStatus).entity(json).build();
//        
//        return response;
//	}
	
	@POST @Path("/v0.1/runcommand")
	@Asynchronous
	@Consumes (MediaType.TEXT_PLAIN)
	public void runcommand(String data, @QueryParam("sessionid") String sessionID, @Suspended AsyncResponse asyncResponse){
		System.out.println("data: " + data);
		System.out.println("sessionID: " + sessionID);
		
		int responseStatus = 200;
        Response response = null;
        Map<String, Object> result = null;
        ObjectMapper mapper = null;
        
		try {
			if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
				responseStatus = 401;
				throw new Exception("sessionID is empty");
			}
			
			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
			if(!sessionIDExists){
				responseStatus = 401;
				throw new Exception("sessionID does not exist");
			}
			
			IMessageSender ratMessageSender = new RATMessageSender(); 
			ratMessageSender.sendMessage(asyncResponse, _context, sessionID, data);
		}
		catch (Exception e) {
			// TODO log
			if(responseStatus == 200){
				responseStatus = 500;
			}
			
			result = new HashMap<String, Object>();
		
	    	String json = null;
	    	
	        result.put(RATConstants.StatusCode, responseStatus);
	        if(mapper == null){
	        	mapper = new ObjectMapper();
	        	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        }
	        
			try {
				json = mapper.writeValueAsString(result);
				System.out.println(json);
			} 
			catch (JsonProcessingException ex) {
				// TODO: gestire l'errore creando un JSON con errore e statuscode 
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
	    	response = Response.status(responseStatus).entity(json).build();
	    	asyncResponse.resume(result);
		}
	}
	
	@POST @Path("/v0.1/runquery")
	@Consumes (MediaType.TEXT_PLAIN)
	@Asynchronous
	public void runquery(String data, @QueryParam("sessionid") String sessionID, @QueryParam("roleName") String roleName, @Suspended final AsyncResponse asyncResponse) {
		System.out.println("data: " + data);
		System.out.println("sessionID: " + sessionID);
		
		int responseStatus = 200;
        Response response = null;
        Map<String, Object> result = null;
        ObjectMapper mapper = null;
        
		try {
			if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
				responseStatus = 401;
				throw new Exception("sessionID is empty");
			}
			
			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
			if(!sessionIDExists){
				responseStatus = 401;
				throw new Exception("sessionID does not exist");
			}
			
			IMessageSender ratMessageSender = new RATMessageSender(); 
			ratMessageSender.sendMessage(asyncResponse, _context, sessionID, data);
		}
		catch (Exception e) {
			// TODO log
			if(responseStatus == 200){
				responseStatus = 500;
			}
			
			result = new HashMap<String, Object>();
		
	    	String json = null;
	    	
	        result.put(RATConstants.StatusCode, responseStatus);
	        if(mapper == null){
	        	mapper = new ObjectMapper();
	        	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        }
	        
			try {
				json = mapper.writeValueAsString(result);
				System.out.println(json);
			} 
			catch (JsonProcessingException ex) {
				// TODO: gestire l'errore creando un JSON con errore e statuscode 
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
	    	response = Response.status(responseStatus).entity(json).build();
	    	asyncResponse.resume(result);
		}
	}
}
