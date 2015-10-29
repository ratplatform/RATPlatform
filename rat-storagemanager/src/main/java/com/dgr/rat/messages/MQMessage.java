/**
 * @author Daniele Grignani (dgr)
 * @date Jun 6, 2015
 */

package com.dgr.rat.messages;


public class MQMessage {
	private String _responseMessage = null;
	
	public MQMessage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the _statusCode
	 */
	public String getResponseMessage() {
		return _responseMessage;
	}

	/**
	 * @param _statusCode the _statusCode to set
	 */
	public void setResponseMessage(String responseMessage) {
		this._responseMessage = responseMessage;
	}

}
