/**
 * @author: Daniele Grignani
 * @date: Nov 4, 2015
 */

package com.dgr.rat.messages;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.utils.MakeAlchemyJSON;
import com.dgr.rat.json.KeepAliveHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.session.manager.RATSessionManager;
import com.dgr.rat.webservices.RATWebServicesContextListener;

// TODO: classe lasciata a metà: da completare la gestione del multithreading; ora ogni chiamata al web service 
// crea una nuova istanza di questa classe e con essa un nuovo _pool con un singolo thread. Se avessi 1000 chiamate avrei 1000 thread.
// Da implementare un reactor
public class RATMessageSender implements IMessageSender{
	private ExecutorService _messageSenderExecutor = Executors.newSingleThreadExecutor();
	private CompletionService<String> _pool = new ExecutorCompletionService<String>(_messageSenderExecutor);
	
	public RATMessageSender() {
		System.out.println("RATMessageSender Send");
	}
	
	// TODO da rivedere l'ho messa solo per la login
	public String sendMessage(final ServletContext servletContext, final String sessionID, final String data) {
		StatusCode responseStatus = StatusCode.Ok;
		String result = null;
		
		try {
	   		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
				responseStatus = StatusCode.Unauthorized;
				throw new Exception("sessionID is empty");
			}
			
			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
			if(!sessionIDExists){
				responseStatus = StatusCode.Unauthorized;
				throw new Exception("sessionID does not exist");
			}
			
//			Subject requestSubject = new Subject.Builder().sessionId(sessionID).buildSubject();
//			System.out.println("Is Authenticated = " + requestSubject.isAuthenticated());
			
			// TODO: attenzione, gestisco la sessione shiro in modo separato e quando la mia
			// piattaforma cancella la sessione conrrente,, dovrebbe anche eseguire il 
			// logout da shiro ASSOLUTAMENTE. Per ora non lo faccio, ma andrà fatto (se shiro verrà mantenuto).
	//	    			boolean isPermitted = requestSubject.isPermitted(action);
	//	    			responseStatus = isPermitted ? StatusCode.Ok : StatusCode.Unauthorized;
	//	    			System.out.println("createCollaborationDomain responseStatus " + responseStatus);
			
	    	FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) servletContext.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
	    	
			MessageSender messageSender = (MessageSender)context.getBean("messageSender");
			messageSender.setMessage(data);
			messageSender.setSessionID(sessionID);
	    	_pool.submit(messageSender);
		
			Future<String>task = _pool.poll(500, TimeUnit.MILLISECONDS);
			if(task != null){
				result = task.get();
				// TODO: sistema di saltare la trasformazione di json davvero brutale: da rivedere
//				if(result.contains("vertices")){
//					result = MakeAlchemyJSON.fromRatJsonToAlchemy(result);
//				}
				System.out.println("result = " + result);
			}
			else{
				responseStatus = StatusCode.RequestTimeout;
				throw new Exception("Future<String>task is null!");
			}
		}
		catch(Exception e){
			if(responseStatus.compareTo(StatusCode.Ok) == 0){
				e.printStackTrace();
				responseStatus = StatusCode.InternalServerError;
			}
			
			JsonHeader jsonHeader = RATJsonUtils.getJsonHeader(responseStatus, MessageType.Response);
			result = KeepAliveHelpers.serializeKeepAliveJson(jsonHeader);
			
			e.printStackTrace();
			// TODO log
		}
		
		//int response = responseStatus.getValue();
		//asyncResponse.resume(Response.status(response).entity(result).build());
		
		return result;
	}

	public void sendMessage(final AsyncResponse asyncResponse, final ServletContext servletContext, final String sessionID, final String data) {
		
        //new Thread(new Runnable() {
            //@Override
            //public void run() {
            	StatusCode responseStatus = StatusCode.Ok;
            	String result = null;
            	
            	try {
	           		if(sessionID == null || sessionID == "" || sessionID.length() < 1 || sessionID == "null"){
	        			responseStatus = StatusCode.Unauthorized;
	    				throw new Exception("sessionID is empty");
	        		}
	        		
	    			boolean sessionIDExists = RATSessionManager.getInstance().sessionIDExists(sessionID);
	    			if(!sessionIDExists){
	    				responseStatus = StatusCode.Unauthorized;
	    				throw new Exception("sessionID does not exist");
	    			}
	    			
//	    			Subject requestSubject = new Subject.Builder().sessionId(sessionID).buildSubject();
//	    			System.out.println("Is Authenticated = " + requestSubject.isAuthenticated());
	    			
	    			// TODO: attenzione, gestisco la sessione shiro in modo separato e quando la mia
	    			// piattaforma cancella la sessione conrrente,, dovrebbe anche eseguire il 
	    			// logout da shiro ASSOLUTAMENTE. Per ora non lo faccio, ma andrà fatto (se shiro verrà mantenuto).
//	    			boolean isPermitted = requestSubject.isPermitted(action);
//	    			responseStatus = isPermitted ? StatusCode.Ok : StatusCode.Unauthorized;
//	    			System.out.println("createCollaborationDomain responseStatus " + responseStatus);
	    			
	            	FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) servletContext.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
	            	
	        		MessageSender messageSender = (MessageSender)context.getBean("messageSender");
	        		messageSender.setMessage(data);
	        		messageSender.setSessionID(sessionID);
	            	_pool.submit(messageSender);
            	
            		Future<String>task = _pool.poll(1000, TimeUnit.MILLISECONDS);
	        		if(task != null){
	        			result = task.get();
	        			// TODO: sistema di saltare la trasformazione di json davvero brutale: da rivedere
//	        			if(result.contains("vertices")){
//	        				result = MakeAlchemyJSON.fromRatJsonToAlchemy(result);
//	        			}
	        			System.out.println("result = " + result);
	        		}
	        		else{
	        			responseStatus = StatusCode.RequestTimeout;
	        			throw new Exception("Future<String>task is null!");
	        		}
            	}
        		catch(Exception e){
        			if(responseStatus.compareTo(StatusCode.Ok) == 0){
        				e.printStackTrace();
        				responseStatus = StatusCode.InternalServerError;
        			}
        			
        			JsonHeader jsonHeader = RATJsonUtils.getJsonHeader(responseStatus, MessageType.Response);
        			result = KeepAliveHelpers.serializeKeepAliveJson(jsonHeader);
        			
        			e.printStackTrace();
        			// TODO log
        		}
            	
            	int response = responseStatus.getValue();
            	asyncResponse.resume(Response.status(response).entity(result).build());
            //}

        //}, "async-runner-" + sessionID).start();
	}
	
	public void shutdown() throws Exception{
		System.out.println("Start shutdown ....");
		 try {
			_messageSenderExecutor.shutdown();
			if (!_messageSenderExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
				_messageSenderExecutor.shutdownNow();
			}
		 } 
		 catch (InterruptedException e) {
			 _messageSenderExecutor.shutdownNow();
			 throw new Exception(e);
		}
	}
}
