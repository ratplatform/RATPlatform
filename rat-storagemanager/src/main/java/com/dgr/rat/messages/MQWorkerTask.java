/**
 * @author Daniele Grignani (dgr)
 * @date Jun 4, 2015
 */

package com.dgr.rat.messages;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.springframework.jms.JmsException;
import com.dgr.rat.async.dispatcher.ITaskCommand;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;

public class MQWorkerTask implements ITaskCommand<MQMessage>{
	private Message _message = null;
	private RATMessagingService _owner = null;

	public MQWorkerTask(Message message, RATMessagingService owner) {
		_message = message;
		_owner = owner;
	}
	
	@Override
	public MQMessage execute(){
		return this.run();
	}
	
	// Anche in caso di errore cerco di mandare un messaggio 
	// comunque.
	// TODO: da rivedere implementando un pattern state
	private MQMessage run(){
		// TODO: MQMessage è mal gestito
		MQMessage result = new MQMessage();
		
		TextMessage txtMsg = (TextMessage) _message;
		String sessionID = null;
		MessageGenerator messageGenerator = null;
		String messageText = null;
		StatusCode commandResult = StatusCode.Unknown;
		StatusCode status = StatusCode.Unknown; // usato come flag per andare avanti
//		JSONObjectBuilder jsonResponse = null;
//		Response response = null;
		String ratJsonResponse = null;
		
		// COMMENT: primo caso (grave): se qui viene lanciata unn'Exception
		// non c'è nulla da fare e il messaggio
		// non può essere inviato in alcun modo. Uscire!
		// TODO: sa risolvere in qualche modo questo primo caso: pensare ad un modo per inviare un messaggio in ogni caso!
		// In prima battuta prevedere dall'altro capo della MQ un timeout
		try {
			sessionID = _message.getJMSCorrelationID();
			messageText = txtMsg.getText();
			// Creo subito il messaggio di ritorno
			messageGenerator = new MessageGenerator(sessionID);
			status = StatusCode.Ok;
		} 
		catch (JMSException e) {
			//result.set_statusCode(StatusCode.InternalServerError);
			status = StatusCode.InternalServerError;
//			commandResult = status;
			e.printStackTrace();
			// TODO log
			// TODO: ATTENZIONE in questo caso è un casino perché non posso comunicare 
			// il messaggio di errore di ritorno! Pertanto diventa necessario prevedere dall'altro 
			// capo della MQ un timeout
		}
		
		if(status == StatusCode.Ok){
			CommandSink commandSink = new CommandSink();
			Response response = commandSink.doCommand(messageText);
			ratJsonResponse = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			
			// TODO: poco sicuro da rivedere
			commandResult = StatusCode.fromString(response.getStatusCode());
			//TODO: cosa faccio se l'istruzione precedente fallisse restituendo un StatusCode.Unknown?
			result.setResponseMessage(ratJsonResponse);
		}
		
		// Mando il messaggio di ritorno
		if(status == StatusCode.Ok){
			System.out.println("MessagingServerWorker: messageText: " + messageText);
			messageGenerator.setStatusCode(commandResult);
			messageGenerator.setMessage(ratJsonResponse);
			
			try {
				_owner.getJmsTemplate().send(_message.getJMSReplyTo(), messageGenerator);
			} 
			catch (JmsException | JMSException e) {
				// TODO log
				e.printStackTrace();
				commandResult = StatusCode.InternalServerError;
				// TODO: sa risolvere in qualche modo questo primo caso: pensare ad un modo per inviare un messaggio in ogni caso!
				// In prima battuta prevedere dall'altro capo della MQ un timeout
			}
			
		}

		return result;
	}

}
