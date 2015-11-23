/**
 * @author: Daniele Grignani
 * @date: Nov 14, 2015
 */

package com.dgr.rat.graphgenerator.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.RATJsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BuildQueryJavaScript {
	private String _javaScript = "";
	private String _functions = "";
	private List<String>_settings = new ArrayList<String>();
	private JsonHeader _header = null;
	
	public BuildQueryJavaScript() {
		// TODO Auto-generated constructor stub
	}
	
	private String makeSetting(String field, JsonNode jsonSettingsNode, Map<String, String> functionsMap){
		String result = "\t\t" + field + " : {\n";
		
		Iterator<Entry<String, JsonNode>> it = jsonSettingsNode.fields();
		while(it.hasNext()){
			Entry<String, JsonNode> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue().asText();
			result += "\t\t\t" + key + ":" + "\"" + value + "\",\n";
			if(value.equalsIgnoreCase(RATConstants.VertexContentUndefined)){
				functionsMap.put(field, key);
			}
		}
		int pos = result.lastIndexOf(",");
		result = result.substring(0, pos);
		
		result += "\n\t\t},\n";
		return result;
	}

	private String makeHeader(String commandName, JsonHeader header, RATJsonObject ratJsonObject) {
		String rootVertexUUID = ratJsonObject.getHeaderProperty(RATConstants.RootVertexUUID);
		String commandGraphUUID = ratJsonObject.getHeaderProperty(RATConstants.CommandGraphUUID);
		//String result = "\t" + RATConstants.Header + " : {\n";
		String result = "";
		Map<String, String> properties = header.getHeaderProperties();
		Iterator<String> it = properties.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			String value = null;
			if(field.equalsIgnoreCase(RATConstants.Time)){
				value = "new Date().toUTCString()";
			}
			else if(field.equalsIgnoreCase(RATConstants.RootVertexUUID)){
				value = "\"" + rootVertexUUID + "\"";
			}
			else if(field.equalsIgnoreCase(RATConstants.CommandGraphUUID)){
				value = "\"" + commandGraphUUID + "\"";
			}
			else{
				value = "\"" + properties.get(field) + "\"";
			}
			result += "\t\t" + field + ":" + value + ",\n";
		}
		int pos = result.lastIndexOf(",");
		result = result.substring(0, pos);
		result += "\n";

		return result;
	}
	
	private String makeFunctions(String commandName, Map<String, String> functions, RATJsonObject ratJsonObject){
		String result = "";
		//result += commandName + "Set" + " = function(";// + key + "){\n";
		String params = "currentDomainUUID, ";
		String calls = "";
		int inc = 0;
		Iterator<String>it = functions.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = functions.get(key);
			params += "param" + inc + ", ";
			calls += "\t" + commandName + "." + RATConstants.Settings + "." + key + "." + value + " = " + "param" + inc + ";\n";
			inc++;
		}
		int pos = params.lastIndexOf(",");
		params = params.substring(0, pos);
		params = params.trim();
		result += commandName + "Set" + " = function(" + params + "){\n";
		result += calls;
		result += "\t" + commandName + "." + RATConstants.Header + "." + RATConstants.DomainUUID + " = currentDomainUUID;\n";
		result += "};\n\n";
		
		return result;
	}
	
	public String getJavaScript(){
		String result = null;
		result = _javaScript + "\n/*Public functions*/\n" + _functions;
		
		return result;
	}
	
	public void make(String commandName, RATJsonObject ratJsonObject) throws Exception{
		ObjectMapper mapper = new ObjectMapper();

		_javaScript += "var " + commandName + " = {\n";
		//_javaScript += "\t" + RATConstants.Header + " : " + RATConstants.Header + ",\n";
		_javaScript += "\t" + RATConstants.Header + " : {\n#headerplaceholder#\t},\n";
		_javaScript += "\t" + RATConstants.Settings + " : {\n#placeholder#\n\t}\n";
		_javaScript += "};\n\n";
		
		Map<String, String> functionsMap = new HashMap<String, String>();
		String settings = mapper.writeValueAsString(ratJsonObject.getSettings());
		JsonNode jsonSettingsNode = mapper.readTree(settings);
		
		String jsSettings = "";
		String jsHeader = "";
		
		Iterator<Entry<String, JsonNode>> it = jsonSettingsNode.fields();
		while(it.hasNext()){
			Entry<String, JsonNode> entry = it.next();
			String field = entry.getKey();
			_settings.add(field);
			
			jsHeader = this.makeHeader(commandName, _header, ratJsonObject);
			jsSettings += this.makeSetting(field, entry.getValue(), functionsMap);
		}
		_functions += this.makeFunctions(commandName, functionsMap, ratJsonObject);
		
		int pos = jsSettings.lastIndexOf(",");
		jsSettings = jsSettings.substring(0, pos);
		_javaScript = _javaScript.replace("#headerplaceholder#", jsHeader);
		_javaScript = _javaScript.replace("#placeholder#", jsSettings);
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
}
