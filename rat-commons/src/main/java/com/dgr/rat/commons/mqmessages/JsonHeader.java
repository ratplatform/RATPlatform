/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.commons.mqmessages;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;

public class JsonHeader {
	private Map<String, String>_headerProperties = new HashMap<String, String>();
	
	public JsonHeader() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, String> getHeaderProperties(){
		return _headerProperties; 
	}
	
	public void addHeaderProperty(String key, String value){
		_headerProperties.put(key, value);
	}
	
	public String getHeaderProperty(String key){
		return _headerProperties.get(key);
	}
	
	public String getStatusCode() {
		return this.getHeaderProperty(RATConstants.StatusCode);
	}
	
	public void setStatusCode(StatusCode statusCode) {
		this.addHeaderProperty(RATConstants.StatusCode, statusCode.toString());
	}
	
	public String getMessageType() {
		return this.getHeaderProperty(RATConstants.MessageType);
	}
	
	public void setMessageType(MessageType messageType) {
		this.addHeaderProperty(RATConstants.MessageType, messageType.toString());
	}

	public String getCommandType() {
		return this.getHeaderProperty(RATConstants.CommandType);
	}
	
	public void setCommandType(JSONType commandType) {
		this.addHeaderProperty(RATConstants.CommandType, commandType.toString());
	}
	
	public String getCommandName() {
		return this.getHeaderProperty(RATConstants.CommandName);
	}
	
	public void setCommandName(String commandName) {
		this.addHeaderProperty(RATConstants.CommandName, commandName);
	}
	
	public String getCommandGraphUUID() {
		return this.getHeaderProperty(RATConstants.CommandGraphUUID);
	}
	
	public void setCommandGraphUUID(UUID uuid) {
		this.addHeaderProperty(RATConstants.CommandGraphUUID, uuid.toString());
	}

	public String getRootVertexUUID() {
		return this.getHeaderProperty(RATConstants.RootVertexUUID);
	}
	
	public void setRootVertexUUID(UUID uuid) {
		this.addHeaderProperty(RATConstants.RootVertexUUID, uuid.toString());
	}
	/**
	 * @return the _placeHolder
	 */
	public String getDomainName() {
		return this.getHeaderProperty(RATConstants.DomainName);
	}

	/**
	 * @param _placeHolder the _placeHolder to set
	 */
	public void setDomainName(String domainName) {
		this.addHeaderProperty(RATConstants.DomainName, domainName);
	}

	/**
	 * @return the _applicationName
	 */
	public String getApplicationName() {
		return this.getHeaderProperty(RATConstants.Application);
	}

	/**
	 * @param _applicationName the _applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.addHeaderProperty(RATConstants.Application, applicationName);
	}
	
	/**
	 * @return the _applicationVersion
	 */
	public String getApplicationVersion() {
		return this.getHeaderProperty(RATConstants.ApplicationVersion);
	}

	/**
	 * @param _applicationVersion the _applicationVersion to set
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.addHeaderProperty(RATConstants.ApplicationVersion, applicationVersion);
	}

	/**
	 * @return the _commandVersion
	 */
	public String getCommandVersion() {
		return this.getHeaderProperty(RATConstants.CommandVersion);
	}

	/**
	 * @param _commandVersion the _commandVersion to set
	 */
	public void setCommandVersion(String commandVersion) {
		this.addHeaderProperty(RATConstants.CommandVersion, commandVersion);
	}
}
