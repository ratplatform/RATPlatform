/*
 * @author Daniele Grignani
 * Apr 4, 2015
*/

package com.dgr.rat.session.manager;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;

import com.dgr.rat.messages.MessageSender;

// TODO: tutta questa parte non è fatta benissimo e va rivista interamente: poiché manca completamente il meccanismo di login, per ora 
// la faccio funzionare così.
public class RATSessionManager {
	private ExecutorService _messageSenderExecutor = (ExecutorService)Executors.newFixedThreadPool(10);
	private CompletionService<String> _pool = new ExecutorCompletionService<String>(_messageSenderExecutor);
	
	// TODO mi serve per attivare i sessionworker che creano i sessionmonitor. Per ora ne creo solo uno
	private ExecutorService _sessionWorkerExecutor = (ExecutorService)Executors.newFixedThreadPool(1);
	private BlockingQueue<String> _queue = new ArrayBlockingQueue<String>(1024);
	private ConcurrentMap<String, SessionMonitor> _sharedMap = new ConcurrentHashMap<String, SessionMonitor>();
	private ConcurrentMap<String, SessionData> _sessionDataMap = new ConcurrentHashMap<String, SessionData>();
	private static RATSessionManager _instance = null;
	
	// TODO: vedi nota alla classe TimerTask
	private ScheduledExecutorService _scheduledExecutor = Executors.newScheduledThreadPool(1);
	
	private RATSessionManager() throws Exception {
		TimerTask task = new TimerTask(_sharedMap);
		
		// TODO: vedi nota alla classe TimerTask
		_scheduledExecutor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
		
		_sessionWorkerExecutor.submit(new SessionWorker(this));
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
	
	public synchronized String sendMessage(FileSystemXmlApplicationContext context, String sessionID, String data) throws Exception{
		MessageSender messageSender = (MessageSender)context.getBean("messageSender");
		messageSender.setMessage(data);
		messageSender.setSessionID(sessionID);
		messageSender.setSharedMap(_sharedMap);
		
		_pool.submit(messageSender);
		
		System.out.println("RATSessionManager enter in polling");
		Future<String>task = _pool.poll(500, TimeUnit.MILLISECONDS);
		String result = null;
		if(task != null){
			result = task.get();
		}
		System.out.println("RATSessionManager exit from polling, result: " + result);
		
		return result;
	}
	
//	public synchronized Session getActiveMQSession() throws JMSException{
//		return _messagingClient.getSession();
//	}
	
	// TODO: non sono sicuro che debba essere synchronized, ma siccome l'oggetto RATSessionManager è statico, cioè un singleton, e
	// ogni connessione ad un client web è come se fosse un thread, per sicurezza.
	// Inoltre uso una BlockingQueue che è treadsafe, quindi il synchronized potrebbe essere davvero inutile...
	public synchronized void setSessionData(SessionData sessionData){
		_queue.add(sessionData.getSessionID());
		_sessionDataMap.put(sessionData.getSessionID(), sessionData);
	}
	
	// TODO idem come sopra; e comunque il meccanismo va implementato meglio. Ora i thread 
	// fanno polling sulla coda e si fermano per qualche millisecondo se restituisce null 
	protected synchronized String getSessionID() throws InterruptedException{
		//return _queue.take();
		return _queue.poll();
	}
	
	// TODO: per ora uso questo, ma quando in una versione evoluta, sarebbe il 
	// caso di implementare uno strumento di search più efficiente magari basato su 
	// quicksort (divisione della mappa in due o più parti) magari multithread
	// usando l'esempio del libro "Java 7 concurrency cookbok" 
	// dal titolo "Running multiple tasks and processing the first result"

	protected synchronized void setWorker(SessionMonitor sessionMonitor){
		_sharedMap.put(sessionMonitor.getSessionID(), sessionMonitor);
	}
	
	// TODO: bisognerebbe implementare una classe specializzata
	// nella ricerca, come indicato sopra. Questa classe contiene diversi worker 
	// thread che fanno la ricerca, eseguita però dentro MessageSender
	protected synchronized boolean sessionIDHasMonitor(String sessionID){
		boolean result = false;
		
		if(_sharedMap.containsKey(sessionID)){
			SessionMonitor sessionMonitor = _sharedMap.get(sessionID);
			sessionMonitor.resetTime();
			result = true;
		}
		
		return result;
	}
	
	public synchronized SessionData getSessionData(String sessionID){
		SessionData result = null;
		if(_sessionDataMap.containsKey(sessionID)){
			result = _sessionDataMap.get(sessionID);
		}
		
		return result;
	}
	
//	public synchronized boolean sessionIDExists(String sessionID){
//		boolean result = false;
//		//_sharedMap non viene controllata perché potrebbe non avere ancora l'oggetto
//		if(_queue.contains(sessionID) && _sessionDataMap.containsKey(sessionID)){
//			result = true;
//		}
//		else{
//			// Siccome è fallito il controllo di cui sopra
//			// ripulisco tutte le mappe
//			if(_sessionDataMap.containsKey(sessionID)){
//				_sessionDataMap.remove(sessionID);
//			}
//			if(_queue.contains(sessionID)){
//				_queue.remove(sessionID);
//			}
//			if(_sharedMap.containsKey(sessionID)){
//				_sharedMap.remove(sessionID);
//			}
//		}
//		
//		return result;
//	}
	
	public synchronized boolean sessionIDExists(String sessionID){
		boolean result = false;
		//_sharedMap non viene controllata perché potrebbe non avere ancora l'oggetto
		if(_sessionDataMap.containsKey(sessionID) && _sessionDataMap.containsKey(sessionID)){
			result = true;
		}
		else{
			// Siccome è fallito il controllo di cui sopra
			// ripulisco tutte le mappe (una di esse potrebbe contenere il sessionID)
			if(_sessionDataMap.containsKey(sessionID)){
				_sessionDataMap.remove(sessionID);
			}
			if(_queue.contains(sessionID)){
				_queue.remove(sessionID);
			}
			if(_sharedMap.containsKey(sessionID)){
				_sharedMap.remove(sessionID);
			}
		}
		
		return result;
	}
	
	// TODO: questo task controlla i SessionMonitor più vecchi di 30 minuti e li rimuove.
	// Però il task è brutale e molto semplice: si scorre tutta la mappa. L'azione non 
	// è efficiente ed andrebbe implementato un meccanismo fondato su più thread che si dividono 
	// la mappa ed ognuno lavora su una porzione di essa (modello quicksort)
	
	// TODO: non è detto che serva, in quanto c'è RATWebServiceSessionListener, ma per ora lo tengo, poi vediamo,
	public class TimerTask implements Runnable{
		private ConcurrentMap<String, SessionMonitor> _sharedMap = null;
		public TimerTask(ConcurrentMap<String, SessionMonitor> sharedMap){
			_sharedMap = sharedMap;
		}
		@Override
		public void run() {
			//System.out.println("Start TimerTask");
			Iterator<String> it = _sharedMap.keySet().iterator();
			while(it.hasNext()){
				String sessionID = it.next();
				SessionMonitor sessionMonitor = _sharedMap.get(sessionID);
				//TODO: 1800000 equivalgono a 30 minuti; tuttavia il valore va caricato da file .properties
				if(sessionMonitor.isOld(1800000)){
					System.out.println("TimerTask: removing " + sessionID);
					_sharedMap.remove(sessionID);
					_sessionDataMap.remove(sessionID);
					_queue.remove(sessionID);
				}
			}
		}
		
	}
}
