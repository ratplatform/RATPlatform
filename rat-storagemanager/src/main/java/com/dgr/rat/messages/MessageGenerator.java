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

import com.dgr.rat.commons.constants.MQConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.mqmessages.MQMessageHelpers;
import com.dgr.rat.commons.utils.DateUtils;

public class MessageGenerator implements MessageCreator{
	private String _correlationID = null;
	private StatusCode _statusCode = StatusCode.Unknown;
	private String _message = null;
	
	public MessageGenerator(String correlationID) {
		_correlationID = correlationID;
	}
	
	public void setStatusCode(StatusCode statusCode){
		_statusCode = statusCode;
	}
	
	public void setMessage(String message){
		_message = message;
	}
	
	private String createJSONMessage(){
		MQMessage mqMessage = new MQMessage();
		mqMessage.setSessionID(_correlationID);
		mqMessage.setStatusResponse(_statusCode.toString());
		if(_message != null){
			mqMessage.setTextMessage(_message);
		}
		String date = DateUtils.getNow(MQConstants.DateFormat);
		mqMessage.setDate(date);
		// TODO: da rivedere un po' questa parte, mi convince poco
		// a causa dei field JSON "embedded" nel catch (sono gli
		// stessi di MQMessage)
		String result = null;
		try {
			result = MQMessageHelpers.serialize(mqMessage);
		} 
		catch (Exception e) {
			// TODO log
			e.printStackTrace();
			result = "{\"sessionID\":\"" + _correlationID + "\",\"statusResponse\":\"" + _statusCode.toString() + "\",\"date\":\"" + date + "\"}";
		}

		return result;
	}
	
	@Override
	public Message createMessage(Session session) {
		TextMessage response = null;
		String json = this.createJSONMessage();

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