/*
 * @author Daniele Grignani
 * Apr 6, 2015
*/

package com.dgr.rat.messages;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import com.dgr.rat.session.manager.SessionMonitor;
import javax.jms.DeliveryMode;
import javax.jms.Destination; 
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component("messageSender")
public class MessageSender implements MessageListener, Callable<String>{
	private JmsTemplate _jmsTemplate = null;
	private Destination _destination = null;
	private Destination _response = null; 
	private String _sessionID = null;
	private String _message = null;
	private boolean _stop = false;
	private String _result = null;
	private ConcurrentMap<String, SessionMonitor> _sharedMap = null;
	
	public MessageSender() {

	}
	
	public void setJmsTemplate(JmsTemplate jmsTemplate){
		this._jmsTemplate = jmsTemplate;
	}
	
	public void setDestination(Destination destination){
		this._destination = destination;
	} 
	
	public void setResponse(Destination response) {
		this._response = response;
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			_result = ((TextMessage) message).getText();
			System.out.println("MessageSender: Received result: " + _response);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			// TODO da loggare e gestire
		}
		_stop = true;
	}
	
	private boolean sessionIDExists(String sessionID){
		boolean result = false;
		
		if(getSharedMap().containsKey(sessionID)){
			SessionMonitor sessionMonitor = getSharedMap().get(sessionID);
			sessionMonitor.resetTime();
			result = true;
		}
		
		return result;
	}

	@Override
	public String call() throws Exception {
		System.out.println("MessageSender: looking for sessionID: " + _sessionID);
		if(this.sessionIDExists(_sessionID)){
			this.sendMessage();
		}
		else{
			System.out.println("MessageSender sessionID " + _sessionID + " not found!");
			_result = "error";
			_stop = true;
		}
		
        while (!_stop){
			try {
				Thread.sleep(10);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
		return _result;
	}
	
	private void sendMessage(){
		MessageCreator message = new MessageGenerator();
		try {
			_jmsTemplate.send(_destination, message);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSessionID() {
		return _sessionID;
	}

	public void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		this._message = message;
	}

	public ConcurrentMap<String, SessionMonitor> getSharedMap() {
		return _sharedMap;
	}

	public void setSharedMap(ConcurrentMap<String, SessionMonitor> sharedMap) {
		this._sharedMap = sharedMap;
	}

	public class MessageGenerator implements MessageCreator{
		public MessageGenerator() {
		}
		
		@Override
		public Message createMessage(Session session) throws JMSException {
			TextMessage textMessage = null;
			try{
				textMessage = session.createTextMessage();
//				textMessage.setJMSType("Filetransfer");
				textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		        textMessage.setText(getMessage());
		        textMessage.setJMSCorrelationID(getSessionID());
		        textMessage.setJMSReplyTo(_response);
		        textMessage.setStringProperty("SessionID", getSessionID());
			}
			catch (JMSException e){
				e.printStackTrace();
			}
			
			return textMessage;
		}
	}
}
