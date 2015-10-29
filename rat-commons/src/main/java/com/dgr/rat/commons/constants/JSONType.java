/*
 * @author Daniele Grignani
 * Apr 5, 2015
*/

package com.dgr.rat.commons.constants;

public enum JSONType {
	UserCommands("UserCommands"),
	UserCommandTemplates("UserCommandTemplates"),
	SystemCommands("SystemCommands"),
	ConfigurationCommands("ConfigurationCommands"),
	LoadCommands("LoadCommands"),
	Query("Query"),
	Unknown ("Unknown");
	
	private final String _stringValue;
	
	private JSONType(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
    public static JSONType fromString(String string) {
        if (string != null){
            for (JSONType type : JSONType.values()){
                if (string.equalsIgnoreCase(type.toString())){
                    return type;
                }
            }
        }
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid JSONType.", string));
    }
}
