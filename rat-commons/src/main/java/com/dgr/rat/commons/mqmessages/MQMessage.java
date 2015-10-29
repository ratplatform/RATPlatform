/**
 * @author Daniele Grignani (dgr)
 * @date Jun 2, 2015
 */

package com.dgr.rat.commons.mqmessages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class MQMessage {
	private String _sessionID = null;
	private String _statusResponse = null;
	private String _textMessage = ""; // Potrebbe non esserci alcun messaggio
	private String _date = null;
	
	public MQMessage() {
		// TODO Auto-generated constructor stub
	}
	
    public String getSessionID() {
		return _sessionID;
	}
    
    @JsonProperty("date")
    public void setDate(String date) {
		this._date = date;
	}
    
	public String getDate() {
		return _date;
	}

    @JsonProperty("sessionID")
    public void setSessionID(String sessionID) {
		this._sessionID = sessionID;
	}
    
	public String getStatusResponse() {
		return _statusResponse;
	}

	@JsonProperty("statusResponse")
	public void setStatusResponse(String statusResponse) {
		this._statusResponse = statusResponse;
	}
	
	public String getTextMessage() {
		return _textMessage;
	}

	@JsonProperty("textMessage")
	public void setTextMessage(String textMessage) {
		this._textMessage = textMessage;
	}

}
