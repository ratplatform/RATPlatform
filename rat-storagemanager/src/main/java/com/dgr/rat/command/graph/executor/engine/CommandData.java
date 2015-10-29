/**
 * @author Daniele Grignani (dgr)
 * @date Sep 9, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.UUID;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.IRATJsonObject;
import com.dgr.rat.storage.provider.IStorage;

public class CommandData {
	private IRATJsonObject _header = null;
	private RemoteCommandsContainer _parameters = null;
	private IStorage _storage = null;
	
	public CommandData(IRATJsonObject header, RemoteCommandsContainer instructionsContainer, IStorage storage) {
		_header = header;
		_parameters = instructionsContainer;
		_storage = storage;
	}
	
	public RemoteCommandsContainer getParameters() {
		return _parameters;
	}
	
	public String getCommandName(){
		return _header.getHeaderProperty(RATConstants.CommandName);
	}
	
	public IStorage getStorage(){
		return _storage;
	}
	
	public UUID getCommandUUID(){
		UUID result = null;
		String uuidStr = _header.getHeaderProperty(RATConstants.CommandGraphUUID);
		try{
			result = UUID.fromString(uuidStr);
		}
		catch(IllegalArgumentException e){
			// TODO: log
			result = null;
		}
		
		return result;
	}
}
