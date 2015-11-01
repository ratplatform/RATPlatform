/**
 * @author Daniele Grignani (dgr)
 * @date Jun 7, 2015
 */

package com.dgr.rat.main.init.db;

import com.dgr.rat.async.dispatcher.ITaskCommand;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;

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
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(_json);
		MQMessage result = new MQMessage(response.getHeader());
		
		return result;
	}
}
