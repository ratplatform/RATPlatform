/*
 * @author Daniele Grignani
 * Apr 5, 2015
*/

package com.dgr.rat.commons.constants;

public enum MessageType {
	Request("Request"),
	Response("Response"),
	Template("Template");
	
	private final String _stringValue;
	
	private MessageType(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
    public static MessageType fromString(String string) {
        if (string != null){
            for (MessageType type : MessageType.values()){
                if (string.equalsIgnoreCase(type.toString())){
                    return type;
                }
            }
        }
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid MessageType.", string));
    }
}
