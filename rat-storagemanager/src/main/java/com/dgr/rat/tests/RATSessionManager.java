/*
 * @author Daniele Grignani
 * Apr 4, 2015
*/

package com.dgr.rat.tests;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;

public class RATSessionManager {
	private ExecutorService _messageSenderExecutor = Executors.newFixedThreadPool(10);
	private CompletionService<String> _pool = new ExecutorCompletionService<String>(_messageSenderExecutor);
	private static RATSessionManager _instance = null;
	private ScheduledExecutorService _scheduledExecutor = Executors.newScheduledThreadPool(1);
	
	private RATSessionManager() throws Exception {
	}
	
	public static void init() throws Exception{
		if(_instance == null){
			_instance = new RATSessionManager();
		}
	}
	
	public static RATSessionManager getInstance() throws Exception{
		if(_instance == null){
			// TODO: messaggio e ResourceBundle per la localizzazione
			throw new Exception();
		}
		
		return _instance;
	}
	
	public void shutdown(){
		System.out.println("Start shutdown ....");
		_messageSenderExecutor.shutdown();
		
		// TODO: vedi nota alla classe TimerTask
		_scheduledExecutor.shutdown();
		System.out.println("End shutdown");
	}
	
	public synchronized String sendMessage(FileSystemXmlApplicationContext context, String data) throws Exception{
		MessageSender messageSender = (MessageSender)context.getBean("messageSender");
		messageSender.setMessage(data);
//		messageSender.setSessionID(sessionID);
		
		_pool.submit(messageSender);
		
		System.out.println("RATSessionManager enter in polling");
//		Future<String>task = _pool.poll(500, TimeUnit.MILLISECONDS);
		Future<String>task = _pool.take();
		String result = null;
		if(task != null){
			result = task.get();
		}
		System.out.println("RATSessionManager exit from polling, result: " + result);
		
		return result;
	}
}
