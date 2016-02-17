/*
 * @author Daniele Grignani
 * Apr 5, 2015
*/

package com.dgr.rat.json.utils;


// TODO: devo eliminare i type nei nodi e creare dei nodi che indicano il tipo, quindi linkare i singoli nodi al nodo che indica il tipo. In questo 
// modo anche i tipi diventano configurabili
public enum VertexType {
	Unknown("Unknown"),
	QueryPivot("QueryPivot"),
	Instruction("Instruction"),
	RootDomain("RootDomain"),
	InstructionParameter("InstructionParameter"),
	SystemKey("SystemKey"),
	Comment("Comment"),
	SubComment("SubComment"),
	RootAdminUser("RootAdminUser"),
	User("User"),
	Properties("Properties"),
//	UserName("UserName"),
//	UserPwd("UserPwd"),
	Domain("Domain"),
	UserContent("UserContent");
	
	private final String _stringValue;
	
	private VertexType(final String stringValue) { 
		_stringValue = stringValue; 
	}
	
	public String toString() { 
		return _stringValue; 
	}
	
	public static boolean compare(VertexType type1, VertexType type2){
		boolean result = false;
		if(type1.toString().equalsIgnoreCase(type2.toString())){
			result = true;
		}
		
		return result;
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
