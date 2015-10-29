/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.json.factory;

import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;

public class Response {
	private CommandResponse _commandResult = null;
	private JsonHeader _header = null;
	
	public Response() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCommandGraphUUID(){
		return _header.getCommandGraphUUID();
	}
	
	public String getStatusCode() {
		return _header.getStatusCode();
	}

	/**
	 * @return the _header
	 */
	public JsonHeader getHeader() {
		return _header;
	}

	/**
	 * @param _header the _header to set
	 */
	public void setHeader(JsonHeader header) {
		this._header = header;
	}

	/**
	 * @return the _commandResult
	 */
	public CommandResponse getCommandResponse() {
		return _commandResult;
	}

	/**
	 * @param _commandResult the _commandResult to set
	 */
	public void setCommandResponse(CommandResponse commandResult) {
		this._commandResult = commandResult;
	}
}
