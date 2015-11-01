/*
 * @author Daniele Grignani
 * Mar 26, 2015
*/

package com.dgr.rat.messages;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.jms.core.JmsTemplate;
import com.dgr.rat.async.dispatcher.EventManager;
import com.dgr.rat.async.dispatcher.IDispatcherListener;
import com.dgr.rat.async.dispatcher.Task;
import com.dgr.rat.commons.mqmessages.IResponse;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;

// TODO verificare che non sia meglio implementare un reactor e far girare RATMessagingServer
// in un thread
public class RATMessagingService implements MessageListener, IDispatcherListener<IResponse>{
	// TODO: modificare il numero di threads usando i core che ospitano l'applicazione Runtime.getRuntime().availableProcessors();
	// TODO: inoltre cercare e sostituire tutti i synchronized con reentrantlock
	private ExecutorService _executor = (ExecutorService)Executors.newFixedThreadPool(10);
	
	private JmsTemplate _jmsTemplate = null;
	private Destination _receiver = null; 
	
	public RATMessagingService() throws Exception {
		System.out.println("RATMessagingServer constructor");
	}
	
	public synchronized void setJmsTemplate(JmsTemplate jmsTemplate){
		this._jmsTemplate = jmsTemplate;
	}
	
	public synchronized JmsTemplate getJmsTemplate(){
		return _jmsTemplate;
	}
	
	public void setReceiver(Destination receiver){
		this._receiver = receiver;
	}
	
	// COMMENT: arriva il messaggio dal sender
	@Override
	public void onMessage(Message message) {
		System.out.println("RATMessagingServer: Received message");
		try {
			if (message instanceof TextMessage) {
				MQWorkerTask mqWorker = new MQWorkerTask(message, this);
				Task<IResponse> task = new Task<IResponse>(mqWorker);
				
				// COMMENT: quando il Task ha finito il suo lavoro, mi iunvia un messaggio in onReceive
				task.addListener(this);
				
				// COMMENT: qui potrei utilizzare get e rilevare il messaggio
				EventManager.getInstance().addTask(task);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws Exception {
		System.out.println("RATMessagingServer: shutdown");
		_executor.shutdown();
		// TODO gestire la chiusura dei thread
    }

	/* (non-Javadoc)
	 * @see com.dgr.rat.async.dispatcher.IDispatcherListener#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(IResponse message) {
		// COMMENT: usato solo per controllo in quanto il messaggio di ritorno viene spedito direttamente da MQWorkerTask
		System.out.println("RATMessagingServer.onReceive: message: " + JSONObjectBuilder.serializeCommandResponse(message));
	}
}
