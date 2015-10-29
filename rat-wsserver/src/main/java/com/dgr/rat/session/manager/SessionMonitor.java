/*
 * @author Daniele Grignani
 * Apr 4, 2015
*/

package com.dgr.rat.session.manager;

public class SessionMonitor {
	private String _sessionID = null;
	private long _startTime = 0;
	
	public SessionMonitor(String sessionID) {
		this.setSessionID(sessionID);
		this.resetTime();
	}

	public String getSessionID() {
		return _sessionID;
	}

	private void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}

	public long getTime() {
		return _startTime;
	}
	
	public boolean isOld(long maxTime){
		boolean result = false;
		long millis = System.currentTimeMillis() -_startTime;
		if(millis >= maxTime){
			result = true;
		}
		
		return result;
	}
	
	public void resetTime(){
		_startTime = System.currentTimeMillis();
	}

}
