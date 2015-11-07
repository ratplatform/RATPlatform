/*
 * @author Daniele Grignani
 * Apr 4, 2015
*/

package com.dgr.rat.session.manager;

public class SessionWorker implements Runnable{
	private RATSessionManager _owner = null;
	private boolean _stop = false;
	//private ConcurrentMap<String, SessionMonitor> _sharedMap = null;
	
	public SessionWorker(RATSessionManager owner) {
		_owner = owner;
		//_sharedMap = sharedMap;
	}
	
    @Override
    public void run() {
    	System.out.println("SessionWorker start");
    	try{
    		while(!_stop){
    			//System.out.println("SessionWorker enter getSessionID");
    			// TODO: più che restituire null dovrebbe bloccarsi in attesa che la queue
    			// venga riempita; in tal caso lo sleep non servirebbe più 
	    		String sessionID = _owner.getSessionID();
	    		//System.out.println("SessionWorker exit getSessionID");
	    		if(sessionID != null){
		    		if(!_owner.sessionIDHasMonitor(sessionID)){
		    			System.out.println("SessionWorker creating session monitor with sessionID " + sessionID);
		    			SessionMonitor sessionMonitor = new SessionMonitor(sessionID);
		    			_owner.setWorker(sessionMonitor);
		    		}
	    		}
	    		else{
	    			//System.out.println("SessionWorker getSessionID is empty");
	    			Thread.sleep(50);
	    		}
    		}
    	}
    	catch(Exception e){
    		// TODO log e gestione
    		e.printStackTrace();
    	}
    }
    
    public synchronized void stop(){
    	_stop = true;
    }

}
