/**
 * @author: Daniele Grignani
 * @date: Nov 3, 2015
 */

package com.dgr.rat.session.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.commons.utils.DateUtils;
import com.dgr.rat.json.KeepAliveHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.messages.MessageSender;
import com.dgr.rat.webservices.RATWebServicesContextListener;

public class KeepAliveTask implements Runnable{
	private ServletContext _servletContext = null;
	private Map<String, String>_map = new HashMap<String, String>();
	private ExecutorService _messageSenderExecutor = Executors.newSingleThreadExecutor();
	private CompletionService<String> _pool = new ExecutorCompletionService<String>(_messageSenderExecutor);
	
	public KeepAliveTask(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	@Override
	public void run() {
		System.out.println("Send KeepAlive");
		
		UUID uuid = UUID.randomUUID();
		JsonHeader header = new JsonHeader();
		header.addHeaderProperty(RATConstants.CorrelationID, uuid.toString());
		header.setCommandType(JSONType.KeepAlive);
		header.setMessageType(MessageType.Request);
		header.setApplicationName("RATWSServer");
		String json = KeepAliveHelpers.serializeKeepAliveJson(header);
		
		_map.put(uuid.toString(), header.getDate());
		
    	StatusCode responseStatus = StatusCode.Ok;
    	String result = null;
    	
		try{
			FileSystemXmlApplicationContext context = (FileSystemXmlApplicationContext) _servletContext.getAttribute(RATWebServicesContextListener.MessageSenderContextKey);
    		MessageSender messageSender = (MessageSender)context.getBean("messageSender");
    		messageSender.setMessage(json);
    		messageSender.setSessionID(null);
        	_pool.submit(messageSender);
    	
    		Future<String>task = _pool.poll(500, TimeUnit.MILLISECONDS);
    		if(task != null){
    			result = task.get();
    		}
    		else{
    			responseStatus = StatusCode.RequestTimeout;
    			throw new Exception("Future<String>task is null!");
    		}
		}
		catch (Exception e) {
			if(responseStatus.compareTo(StatusCode.Ok) != 0){
				e.printStackTrace();
				responseStatus = StatusCode.InternalServerError;
			}
			
			JsonHeader jsonHeader = RATJsonUtils.getJsonHeader(responseStatus, MessageType.Response);
			result = KeepAliveHelpers.serializeKeepAliveJson(jsonHeader);
			
			e.printStackTrace();
			// TODO log
		}
		
		this.setMessageReceived(result);
	}

	private void setMessageReceived(final String message) {
		System.out.println(message);
		String statusCode = null;
		String startDate = null;
		// TODO: da rivedere: se lancia un'Exception, dentro _map rimangono dei dati che non verranno mai
		// rimossi
		try {
			JsonHeader header = KeepAliveHelpers.deserializeKeepAliveJson(message);
			statusCode = header.getStatusCode();
			// COMMENT: ora in cui RATStorageManager ha inviato il messaggio di risposta
//			String date = header.getDate();
			String correlationID = header.getHeaderProperty(RATConstants.CorrelationID);
			if(_map.containsKey(correlationID)){
				startDate = _map.remove(correlationID);
			}
		} 
		catch (Exception e) {
			// TODO log
			statusCode = StatusCode.InternalServerError.toString();
			e.printStackTrace();
		}
		
		String now = DateUtils.getNow(RATConstants.DateFormat);
		_servletContext.setAttribute(RATWebServicesContextListener.KeepAliveSent, startDate);
		_servletContext.setAttribute(RATWebServicesContextListener.KeepAliveReceived, now);
		_servletContext.setAttribute(RATWebServicesContextListener.KeepAliveStatus, statusCode);
	}
}
