/**
 * @author Daniele Grignani (dgr)
 * @date Jun 2, 2015
 */

package com.dgr.rat.commons.mqmessages;

import java.util.Map;

import com.dgr.rat.commons.constants.MQConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class MQMessage implements IResponse{
	private JsonHeader _header = null;
	private Map<String, Object> _commandResponseMap = null;
	
	public MQMessage(JsonHeader header) {
		_header = header;
		
		String date = DateUtils.getNow(MQConstants.DateFormat);
		this.setDate(date);
	}
	
	public void setSessionID(String sessionID) {
		_header.addHeaderProperty("sessionID", sessionID);
	}
	
	public String getSessionID(){
		return _header.getHeaderProperty("sessionID");
	}
	
	private void setDate(String date) {
		_header.addHeaderProperty("date", date);
	}
	
	public void setCommandResponse(Map<String, Object> commandResponseMap){
		_commandResponseMap = commandResponseMap;
	}
	
	public void buildResponse(IResponse response){
		_header = response.getHeader();
	}

	@Override
	public JsonHeader getHeader() {
		// TODO Auto-generated method stub
		return _header;
	}

	@Override
	public Map<String, Object> getResult() {
		// TODO Auto-generated method stub
		return _commandResponseMap;
	}

	public void setStatusResponse(StatusCode statusCode) {
		_header.setStatusCode(statusCode);
	}
}
