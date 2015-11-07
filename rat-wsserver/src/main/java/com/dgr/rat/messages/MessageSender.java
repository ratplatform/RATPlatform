/*
 * @author Daniele Grignani
 * Apr 6, 2015
*/

package com.dgr.rat.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import com.dgr.rat.session.manager.SessionMonitor;
import com.dgr.rat.webservices.IDispatcherListener;

import javax.jms.DeliveryMode;
import javax.jms.Destination; 
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component("messageSender")
public class MessageSender implements MessageListener, Runnable{
	private JmsTemplate _jmsTemplate = null;
	private Destination _destination = null;
	private Destination _response = null; 
	private String _sessionID = null;
	private String _message = null;
	private boolean _stop = false;
	private String _result = null;
//	private ConcurrentMap<String, SessionMonitor> _sharedMap = null;
	private List<IDispatcherListener> _listeners = new ArrayList<IDispatcherListener>();
	
	public MessageSender() {

	}
	
	public synchronized boolean addListener(IDispatcherListener subscriber) {
		boolean result = false;
		
		if(!this.listenerExist(subscriber)){
			result = _listeners.add(subscriber);
		}
		
		return result;
	}
	
	public synchronized boolean listenerExist(IDispatcherListener subscriber){
		return _listeners.contains(subscriber);
	}
	
	private void sendMessageToListeners(String message){
		Iterator<IDispatcherListener> it = _listeners.iterator();
		while(it.hasNext()){
			IDispatcherListener listener = it.next();
			listener.onReceive(message);
		}
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
			_result = "error";
			e.printStackTrace();
			// TODO da loggare e gestire
		}

		finally{
			synchronized(this){
				_stop = true;
				notifyAll();
			}
		}
	}

	@Override
	//public String call() throws Exception {
	public void run() {
		System.out.println("MessageSender: looking for sessionID: " + _sessionID);

		try {
			MessageCreator message = new MessageGenerator();
			_jmsTemplate.send(_destination, message);
			
		    synchronized(this){
		        while(!_stop){
		            wait();
		        }
		    }
		} catch (Exception e) {
			_result = "error";
			_stop = true;
			e.printStackTrace();
		}
        
        this.sendMessageToListeners(_result);
        
//        return _result;
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

//	public ConcurrentMap<String, SessionMonitor> getSharedMap() {
//		return _sharedMap;
//	}
//
//	public void setSharedMap(ConcurrentMap<String, SessionMonitor> sharedMap) {
//		this._sharedMap = sharedMap;
//	}

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
