/**
 * @author Daniele Grignani (dgr)
 * @date Jun 7, 2015
 */

package com.dgr.rat.main.init.db;

import com.dgr.rat.async.dispatcher.ITaskCommand;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.messages.MQMessage;

public class InitRATDomainTask implements ITaskCommand<MQMessage>{
	private String _json = null;
	
	public InitRATDomainTask(String json) {
		_json = json;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.async.dispatcher.ITaskCommand#execute()
	 */
	@Override
	public MQMessage execute() {
		MQMessage result = new MQMessage();
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(_json);
		String ratJsonResponse = JSONObjectBuilder.buildJSONRatCommandResponse(response);
		result.setResponseMessage(ratJsonResponse);
		
		return result;
	}
}
