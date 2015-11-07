/*
 * @author Daniele Grignani
 * Mar 18, 2015
*/

package com.dgr.rat.webservices;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.auth.LoginData;
import com.dgr.rat.auth.db.IdentityManager;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.DateUtils;
import com.dgr.rat.login.json.ChooseDomainData;
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
	public static final String STATUS_RESPONSE = "statusResponse";
	//@PersistenceContext(unitName="ratwsserver")
//	private EntityManager _entityManager = RATWebServicesContextListener.createEntityManager(); 
	@Context
	private ServletContext _context; 
	@Context 
	private HttpServletRequest _request;
	
	@Inject
	RATMessageSender _ratMessageSender;
	
	public RATWebServices() {
		System.out.println("RATWebServices");
	}
	
	@GET
	@Asynchronous
	@Produces(MediaType.TEXT_PLAIN)
	public void longRunning(@Suspended final AsyncResponse asyncResponse){
        System.out.println("longRunning");
        
        new Thread(new Runnable() {
            public void run() {
                String result = "WORKING";
                for (int inc = 0; inc < 1000000; inc++){
                	System.out.println(inc);
                }
                asyncResponse.resume(result);
                System.out.println(result);
            }
         }).start();
	}
	
	@POST @Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes (MediaType.TEXT_PLAIN)
	public Response login(String data){
        System.out.println("login");
        System.out.println(data);
        
        int responseStatus = 200;
        Response response = null;
        Map<String, Object> result = null;
        ObjectMapper mapper = null;

        try {
    		mapper = new ObjectMapper();
    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	    LoginData authenticationData = mapper.readValue(data, LoginData.class);
    	    
    		String login = authenticationData.getUserName();
    		String password = authenticationData.getPassword();
            UsernamePasswordToken token = new UsernamePasswordToken(login, password);
            Subject currentUser = SecurityUtils.getSubject();

            token.setRememberMe(true);
            currentUser.login(token); 

            String sessionID = currentUser.getSession().getId().toString();
            System.out.println(sessionID);
            
            SessionData sessionData = new SessionData(sessionID);
            sessionData.setPassword(password);
            sessionData.setUserName(login);
			
			IdentityManager identityManager = new IdentityManager();
			result = identityManager.getUserDomains(sessionID, login);
			responseStatus = 200;
			
			RATSessionManager.getInstance().setSessionData(sessionData);
		} 
        catch (UnknownAccountException e) {
            System.out.println("Incorrect username/password!");
            responseStatus = 401;
            result = new HashMap<String, Object>();
        }
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
        finally{
        	String json = null;
        	
            result.put(STATUS_RESPONSE, String.valueOf(responseStatus));
            if(mapper == null){
            	mapper = new ObjectMapper();
            	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            }
            
			try {
				json = mapper.writeValueAsString(result);
				System.out.println(json);
			} 
			catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	response = Response.status(responseStatus).entity(json).build();
        }
        
        return response;
	}
	
	@POST @Path("/choosedomain")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes (MediaType.TEXT_PLAIN)
	public Response chooseDomain(String data, @QueryParam("sessionid") String sessionID){
        System.out.println("choosedomain");
        System.out.println(data);
        
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
			
    		mapper = new ObjectMapper();
    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	    ChooseDomainData domainData = mapper.readValue(data, ChooseDomainData.class);

			IdentityManager identityManager = new IdentityManager();
			result = identityManager.getDomainRoles(sessionID, domainData.get_userName(), domainData.get_domainName());
			responseStatus = 200;
		} 
        catch (Exception e) {
			// TODO log
			if(responseStatus == 200){
				e.printStackTrace();
				responseStatus = 500;
			}
            result = new HashMap<String, Object>();
		}
        finally{
        	String json = null;
        	
            result.put(STATUS_RESPONSE, String.valueOf(responseStatus));
            if(mapper == null){
            	mapper = new ObjectMapper();
            	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            }
            
			try {
				json = mapper.writeValueAsString(result);
				System.out.println(json);
			} 
			catch (JsonProcessingException ex) {
				ex.printStackTrace();
			}
			
        	response = Response.status(responseStatus).entity(json).build();
        }
        
        return response;
	}
	
	@POST @Path("/createcollaborationdomain")
	@Consumes (MediaType.TEXT_PLAIN)
	@Asynchronous
	public void createCollaborationDomain(String data, @QueryParam("sessionid") String sessionID, @QueryParam("domainName") String domainName, @QueryParam("roleName") String roleName, @Suspended AsyncResponse asyncResponse) {
		System.out.println("createcollaborationdomain id: " + sessionID);
		System.out.println("createcollaborationdomain domainName: " + domainName);
		System.out.println("createcollaborationdomain roleName: " + roleName);
		System.out.println("send createcollaborationdomain");
		
        int responseStatus = 200;
        Map<String, String> result = null;
        ObjectMapper mapper = null;
        String json = null;
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
			
			Subject requestSubject = new Subject.Builder().sessionId(sessionID).buildSubject();
			System.out.println("Is Authenticated = " + requestSubject.isAuthenticated());
			
			// TODO: attenzione, gestisco la sessione shiro in modo separato e quando la mia
			// piattaforma cancella la sessione conrrente,, dovrebbe anche eseguire il 
			// logout da shiro ASSOLUTAMENTE. Per ora non lo faccio, ma andrà fatto (se shiro verrà mantenuto).
			boolean isPermitted = requestSubject.isPermitted("createcollaborationdomain");
			responseStatus = isPermitted ? 200 : 401;
			System.out.println("createCollaborationDomain responseStatus " + responseStatus);

			FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) _context.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
//			json = RATSessionManager.getInstance().sendMessage(context, sessionID, data);
			WSAsyncResponse listener = new WSAsyncResponse();
			_ratMessageSender.sendMessage(listener, context, sessionID, data);

			json = listener.getMessage();
			
			System.out.println("RATWebServices createcollaborationdomain response received; message: " + json);
		} 
        catch (Exception e) {
			// TODO log
			if(responseStatus == 200){
				e.printStackTrace();
				responseStatus = 500;
			}
			String date = DateUtils.getNow(RATConstants.DateFormat);
			json = "{\"sessionID\":\"" + sessionID + "\",\"statusResponse\":\"" + String.valueOf(responseStatus) + "\",\"date\":\"" + date + "\"}";
			
			e.printStackTrace();
		}
        finally{
			asyncResponse.resume(Response.status(responseStatus).entity(json).build());
        }
	}
	
//	@POST @Path("/choosedomain")
//	@Produces(MediaType.TEXT_PLAIN)
//	@Consumes (MediaType.TEXT_PLAIN)
//	public Response chooseDomain(String data, @QueryParam("sessionid") String sessionID){
//        System.out.println("choosedomain");
//        System.out.println(data);
//        
//        Response response = null;
//        
//		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
//			// TODO: da aggiungere messaggio
//			response = Response.status(500).entity("").build();
//			return response;
//		}
//		
//        //String sessionID = _request.getSession().getId();
//        try {
//			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
//			if(!sessionIDExists){
//				// TODO aggiungere messaggio
//				throw new Exception();
//			}
//			
//    		ObjectMapper mapper = new ObjectMapper();
//    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//    	    //String json = "{\"sessionID\":\"" + loginResponse.get_sessionID() + "\",\"domainName\":\"RAT\",\"domainID\":\"1\"}";
//    	    ChooseDomainData domainData = mapper.readValue(data, ChooseDomainData.class);
//    	    
//    	    //SessionData sessionData = RATSessionManager.getInstance().getSessionData(sessionID);
//    	    
//    		//String userName = sessionData.getUserName();
//    		//String password = sessionData.getPassword();
//            //UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
//            //Subject currentUser = SecurityUtils.getSubject();
//
//            try {
//                //currentUser.login(token);   //This throws an error upon form submission
//    			IdentityManager identityManager = new IdentityManager();
//    			String json = identityManager.getDomainRoles(sessionID, domainData.get_userName(), domainData.get_domainName());
//    			response = Response.status(200).entity(json).build();
//            } 
//            catch (IncorrectCredentialsException ice) {
//                //System.out.println("Incorrect username/password!");
//                //throw new Exception("Incorrect username/password!");
//            	throw new Exception();
//            }
//		} 
//        catch (Exception e) {
//			// TODO log e gestione
//			e.printStackTrace();
//			// TODO: da gestire meglio
//			response = Response.status(500).entity(e.getMessage()).build();
//		}
//        
//        return response;
//	}
	
//	@POST @Path("/createcollaborationdomain")
//	@Consumes (MediaType.TEXT_PLAIN)
//	@Asynchronous
//	//@Produces ("text/plain")
//	public void createCollaborationDomain(String data, @QueryParam("sessionid") String sessionID, @QueryParam("roleid") String roleID, @Suspended AsyncResponse asyncResponse) {
//		System.out.println("createcollaborationdomain id: " + sessionID);
//		
//		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
//			asyncResponse.resume(Response.status(401));
//			return;
//		}
//		
//		System.out.println("send createcollaborationdomain");
//		try {
//			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
//			if(!sessionIDExists){
//				// TODO da gestire meglio
//				throw new Exception();
//			}
//			
//			Subject currentUser = SecurityUtils.getSubject();
//			boolean isPermitted = currentUser.isPermitted(roleID);
//			if(isPermitted){
//				System.out.println("Is permitted");
//			}
//			else{
//				System.out.println("Is not permitted");
//			}
//			
//			asyncResponse.resume(Response.ok("").build());
//			
////			SessionData sessionData = RATSessionManager.getInstance().getSessionData(sessionID);
////    		String login = sessionData.getUserName();
////    		String password = sessionData.getPassword();
////            UsernamePasswordToken token = new UsernamePasswordToken(login, password);
////            Subject currentUser = SecurityUtils.getSubject();
////			
////			FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) _context.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
////			String result = RATSessionManager.getInstance().sendMessage(context, sessionID, data);
////			System.out.println("RATWebServices createcollaborationdomain response received");
////			asyncResponse.resume(result);
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			asyncResponse.resume( Response.status(500).entity(e.getMessage()).build());
//		}
//	}
	// TODO: ogni web service invia un messaggio e tuttto viene gestito da un unico thread. Devo verificare che ogni web service riceva la risposta giusta.
	// Infatti non vorrei che:
	// il ws A invia una richiesta
	// il ws B invia una richiesta mentre quella di a non è ancora processata
	// la risposta per B è pronta prima di quella di A, tuttavia A è in attesa da prima di B
	// quindi A riceve la risposta di B.
	
	/*
	 Devo poter garantire che tutti i messaggi dal client vengano eseguiti in sequenza, specie se decido di usare activeMQ come una specie di loadbalancer
	 con più server connessi dove il primo prende il messaggio, lo cancella dalla coda e lo esegue.
	 Per fare ciò devo prevedere un sistema di cache lato server (da capire se qui o sul server che gestisce il db, più probabile qui) che ordina i messaggi e li esegue 
	 in sequenza, cioè se mi arriva un messaggio e non ho ancora ricevuto il precedente, devo allora metterlo in cache in attesa di quello formalmente precedente.
	 Ciò è particolarmente utile soprattutto se uso la persistenza dei messaggi di ActiveMQ. 
	 Per segnalare la chiusura della connessione col client, posso usare il timeout della sessione web. Quando scatta il timeout della sessione, so che non arriveranno più messaggi.
	 Lato client devo immaginare un meccanismo di creazione dei numeri di sequenza dei messaggi. L'ID del messaggio deve essere composto da: session-id + numero di sequenza.  
	 */
	
	@POST @Path("/executeconfigurationcommand")
	@Asynchronous
	@Consumes (MediaType.TEXT_PLAIN)
	public void executeconfigurationcommand(String data, @QueryParam("sessionid") String sessionID, @Suspended AsyncResponse asyncResponse){
//		System.out.println("executeconfigurationcommand");
//		//System.out.println(data);
//		System.out.println(sessionID);
//		
//		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
//			asyncResponse.resume(Response.status(401));
//			return;
//		}
//		
//		// TODO: fare attenzione, con la mia pagina di test genero una sessione nuova ad ogni chiamata del web service
//		// quindi per ora, fino a che non capirò il meccanismo per bene, vado a generare un uuid
//		//System.out.println("SessionID: " + _request.getSession().getId());
//		
//		System.out.println("send executeconfigurationcommand");
//		try {
//			FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) _context.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
//			String result = RATSessionManager.getInstance().sendMessage(context, sessionID, data);
//			System.out.println("RATWebServices executeconfigurationcommand response received");
//			asyncResponse.resume(result);
//		} 
//		catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
////		// TODO: capire se conviene fare queste operazioni nel costruttore e non ogni volta che viene chiamato il servizio
////		RATMessagingClient messageProducer = (RATMessagingClient) _context.getAttribute(RATWebServicesContextListener.MessageSender);
////		@SuppressWarnings("unchecked")
////		CompletionService<String> pool = (CompletionService<String>)_context.getAttribute(RATWebServicesContextListener.ThreadPool);
////		pool.submit(new MessageSender(messageProducer, data));
////
////		try {
////			String result = pool.take().get();
////			asyncResponse.resume(result);
////		} 
////		catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} 
////		catch (ExecutionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
	}
	
	@POST @Path("/createnewuser")
	@Consumes (MediaType.TEXT_PLAIN)
	@Asynchronous
	public void createNewUser(String data, @QueryParam("sessionid") String sessionID, @Suspended AsyncResponse asyncResponse) {
//		System.out.println("createnewuser id: " + sessionID);
//		
//		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
//			asyncResponse.resume(Response.status(401));
//			return;
//		}
//		
//		System.out.println("send createnewuser");
//		try {
//			FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) _context.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
//			String result = RATSessionManager.getInstance().sendMessage(context, sessionID, data);
//			System.out.println("RATWebServices createnewuser response received");
//			asyncResponse.resume(result);
//		} 
//		catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
