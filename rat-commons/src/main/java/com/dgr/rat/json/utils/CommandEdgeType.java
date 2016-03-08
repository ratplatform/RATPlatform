/**
 * @author Daniele Grignani (dgr)
 * @date Sep 4, 2015
 */

package com.dgr.rat.json.utils;

public enum CommandEdgeType {
	Instruction("Instruction"),
	InstructionParameter("InstructionParameter"),
	UserName("UserName"),
	Password("Password");
	
	private final String _stringValue;
	
	private CommandEdgeType(final String stringValue) { 
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
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid CommandGraphType.", string));
    }
}
