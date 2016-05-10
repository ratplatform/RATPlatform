/**
 * @author Daniele Grignani (dgr)
 * @date Sep 3, 2015
 */

package com.dgr.rat.json.utils;

public enum ReturnType {
	unknown("unknown"),
	none("none"),
	string("string"),
	integer("integer"),
	uuid("uuid"),
	bool("bool"),
	systemKey("systemKey"),
	url("url"),
	StringArray("stringArray");
	
	private final String _stringValue;
	
	private ReturnType(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
    public static ReturnType fromString(String string) throws IllegalArgumentException{
        if (string != null){
            for (ReturnType type : ReturnType.values()){
                if (string.equalsIgnoreCase(type.toString())){
                    return type;
                }
            }
        }
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid ReturnType.", string));
    }
}
