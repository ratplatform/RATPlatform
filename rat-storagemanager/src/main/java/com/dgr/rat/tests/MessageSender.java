/*
 * @author Daniele Grignani
 * Apr 6, 2015
*/

package com.dgr.rat.tests;

import java.util.concurrent.Callable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
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

	@Override
	public String call() throws Exception {
		
		this.sendMessage();
		
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
