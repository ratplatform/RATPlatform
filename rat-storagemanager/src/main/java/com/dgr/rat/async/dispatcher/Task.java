/**
 * @author Daniele Grignani (dgr)
 * @date Jun 4, 2015
 */

package com.dgr.rat.async.dispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class Task<V> implements Callable<V>{
	private ITaskCommand<V> _taskCommand = null;
	private List<IDispatcherListener<V>> _listeners = new ArrayList<IDispatcherListener<V>>();
	
	public Task(ITaskCommand<V> taskCommand) {
		_taskCommand = taskCommand;
	}
	
	protected ITaskCommand<V> getTaskCommand(){
		return _taskCommand;
	}
	
	protected void sendMessage(V message){
		Iterator<IDispatcherListener<V>> it = _listeners.iterator();
		while(it.hasNext()){
			IDispatcherListener<V> listener = it.next();
			listener.onReceive(message);
		}
	}
	
	protected boolean hasListeners(){
		return _listeners.size() > 0 ? true : false;
	}
	
	public synchronized boolean addListener(IDispatcherListener<V> subscriber) {
		boolean result = false;
		
		if(!this.listenerExist(subscriber)){
			result = _listeners.add(subscriber);
		}
		
		return result;
	}
	
	public synchronized boolean listenerExist(IDispatcherListener<V> subscriber){
		return _listeners.contains(subscriber);
	}
	
	// COMMENT: In questo modo posso usarlo in due modi diversi:
	// 1) con sendMessage nel caso di gestione degli eventi via sendMessage (ad es.: RATMessagingService.onReceive riceve i messaggi qui prodotti)
	// 2) con Future.get (in RATMessagingService.onMessage)
	@Override
	public V call() throws Exception {
		V message = this.getTaskCommand().execute();
	
		if(this.hasListeners()){
			this.sendMessage(message);
		}
		
		// COMMENT: usato con Future.get
		return message;
	}
}
