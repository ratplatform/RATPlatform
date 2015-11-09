/**
 * @author: Daniele Grignani
 * @date: Nov 8, 2015
 */

package com.dgr.rat.json.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.IResultDriller;
import com.dgr.rat.commons.constants.RATConstants;

public class KeepAliveDriller implements IResultDriller{
	private Map<String, Object> _commandResponsePropertiesMap = new HashMap<String, Object>();
	
	public KeepAliveDriller() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.IResultDriller#drill(com.dgr.rat.command.graph.executor.engine.result.CommandResponse)
	 */
	@Override
	public void drill(CommandResponse result) throws Exception {
		_commandResponsePropertiesMap.put(RATConstants.CommandType, result.getCommandType());
		_commandResponsePropertiesMap.put(RATConstants.CommandName, result.getCommandName());
	}

	@Override
	public Map<String, Object> getResult(){
		return _commandResponsePropertiesMap;
	}

	public Object getProperty(String key){
		return _commandResponsePropertiesMap.get(key);
	}
	
	public Set<String>getPropertyKeys(){
		return _commandResponsePropertiesMap.keySet();
	}
	
	public Iterator<String>getKeyIterator(){
		return this.getPropertyKeys().iterator();
	}

}
