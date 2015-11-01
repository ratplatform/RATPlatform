/*
 * @author Daniele Grignani
 * Apr 7, 2015
*/

package com.dgr.rat.messages;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.springframework.jms.core.MessageCreator;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;

public class MessageGenerator implements MessageCreator{
	private String _correlationID = null;
	private MQMessage _message = null;
	public MessageGenerator(MQMessage message) {
		_message = message;
		_correlationID = message.getSessionID();
	}
	
	@Override
	public Message createMessage(Session session) {
		TextMessage response = null;
		String json = JSONObjectBuilder.serializeCommandResponse(_message);

		try{
			response = session.createTextMessage();
			response.setText(json);
			response.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			response.setJMSCorrelationID(_correlationID);
		}
		catch (JMSException e){
			// TODO log
			// In questo caso è un casino e response è null e c'è poco da fare
			e.printStackTrace();
		}
		
		return response;
	}
}