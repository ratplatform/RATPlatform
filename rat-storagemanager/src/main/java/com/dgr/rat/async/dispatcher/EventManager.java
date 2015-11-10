/**
 * @author Daniele Grignani (dgr)
 * @date Jun 3, 2015
 */

package com.dgr.rat.async.dispatcher;

// TODO da modificare rendendolo un daemon executor con timer
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventManager {
	private static EventManager _instance = null;
	//private BlockingQueue<Task<?>> _queue = new ArrayBlockingQueue<Task<?>>(1024);
	// TODO: inoltre cercare e sostituire tutti i synchronized con reentrantlock
	private final int _numberOfCores = Runtime.getRuntime().availableProcessors();
	private ExecutorService _executor = (ExecutorService)Executors.newFixedThreadPool(1);
	
	private EventManager() {
	}
	
	public <T> Future<T> addTask(Task<T> task){
		return _executor.submit(task);
	}
	
	// Da usare nel caso di utilizzo di IDispatcherListener
//	public void addTask(Task<?> task){
//		//_queue.add(task)
//		_executor.submit(task);
//	}
	
	public static EventManager getInstance(){
		if(_instance == null){
			_instance = new EventManager();
		}
		
		return _instance;
	}
}
