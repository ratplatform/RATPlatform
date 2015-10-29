/*
 * @author Daniele Grignani
 * Apr 5, 2015
*/

package com.dgr.rat.storage.provider;

public enum StorageType {
	TinkerGraph("TinkerGraph"),
	OrientDB("OrientDB"),
	Unknown ("Unknown");
	
	private final String _stringValue;
	
	private StorageType(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
    public static StorageType fromString(String string) {
        if (string != null){
            for (StorageType type : StorageType.values()){
                if (string.equalsIgnoreCase(type.toString())){
                    return type;
                }
            }
        }
        
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid StorageType.", string));
    }
}
