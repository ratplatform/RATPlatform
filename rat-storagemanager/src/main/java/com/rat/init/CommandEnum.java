package com.rat.init;

import com.dgr.rat.json.utils.VertexType;

public enum CommandEnum {
	UserName("UserName"),
	Password("Password"),
	TestPassword("TestPassword"),
	UserEmail("UserEmail"),
	Domain("Domain"),
	Create("Create")
	;
	
	private final String _stringValue;
	
	private CommandEnum(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
    public static VertexType fromString(String string) {
        if (string != null){
            for (VertexType type : VertexType.values()){
                if (string.equalsIgnoreCase(type.toString())){
                    return type;
                }
            }
        }
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid Command.", string));
    }
}
