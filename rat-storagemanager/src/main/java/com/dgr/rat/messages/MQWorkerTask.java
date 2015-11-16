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
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.mqmessages.IResponse;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;

public class MQWorkerTask implements ITaskCommand<IResponse>{
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
		MQMessage result = null;
		
		TextMessage txtMsg = (TextMessage) _message;
		String sessionID = null;
		String messageText = null;
		StatusCode status = StatusCode.Unknown; // usato come flag per andare avanti
		StatusCode commandResult = StatusCode.Unknown;
		
		// COMMENT: primo caso (grave): se qui viene lanciata un'Exception
		// non c'è nulla da fare e il messaggio
		// non può essere inviato in alcun modo. Uscire!
		// TODO: sa risolvere in qualche modo questo primo caso: pensare ad un modo per inviare un messaggio in ogni caso!
		// In prima battuta prevedere dall'altro capo della MQ un timeout
		try {
			sessionID = _message.getJMSCorrelationID();
			messageText = txtMsg.getText();
			status = StatusCode.Ok;
		} 
		catch (JMSException e) {
			status = StatusCode.InternalServerError;
			e.printStackTrace();
			// TODO log
			// TODO: ATTENZIONE in questo caso è un casino perché non posso comunicare 
			// il messaggio di errore di ritorno! Pertanto diventa necessario prevedere dall'altro 
			// capo della MQ un timeout
		}
		
		if(status == StatusCode.Ok){
			
			CommandSink commandSink = new CommandSink();
			Response response = commandSink.doCommand(messageText);
			
			// TODO: poco sicuro da rivedere
			commandResult = StatusCode.fromString(response.getStatusCode());
			
			result = new MQMessage(response.getHeader());
			result.setCommandResponse(response.getResult());
			result.setSessionID(sessionID);
			
			//TODO: cosa faccio se l'istruzione precedente fallisse restituendo un StatusCode.Unknown?
			//System.out.println("MessagingServerWorker: messageText: " + messageText);
			MessageGenerator messageGenerator = new MessageGenerator(result);
			
			try {
				// COMMENT: invio della risposta al sender
				System.out.println("Send reply");
				_owner.getJmsTemplate().send(_message.getJMSReplyTo(), messageGenerator);
				System.out.println("Reply sent");
			} 
			catch (JmsException | JMSException e) {
				// TODO log
				e.printStackTrace();
				commandResult = StatusCode.InternalServerError;
				result.setStatusResponse(commandResult);
				// TODO: sa risolvere in qualche modo questo primo caso: pensare ad un modo per inviare un messaggio in ogni caso!
				// In prima battuta prevedere dall'altro capo della MQ un timeout
			}
			
		}
		
		// COMMENT: c'è stato un problema grave....
		if(result == null){
			JsonHeader header = RATJsonUtils.getJsonHeader(commandResult, MessageType.Response);

			result = new MQMessage(header);
		}

		// COMMENT: restituisco in ogni caso un messaggio, anche se di fatto quello al sender 
		// viene inviato con _owner.getJmsTemplate().send(_message.getJMSReplyTo(), messageGenerator),
		// in quanto dentro RATMessagingService.onReceive potrei voler fare dei controlli, oppure altre operazioni.
		// In altre parole, il messaggio qui restituito viene recuperato da Task<V> ed inviato:
		// 1) via sendMessage a tutti i suoi listener
		// 2) restituito al chiamate quando viene fatto Task.get
		return result;
	}
}
