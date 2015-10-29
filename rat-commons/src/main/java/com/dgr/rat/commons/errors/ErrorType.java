/*
 * @author Daniele Grignani
 * Apr 12), 2015
*/

package com.dgr.rat.commons.errors;

public enum ErrorType {
    BAD_REQUEST(400),
    UNAUTOTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    REQUEST_TIME_OUT(408),
    CONFLICT(409),
    VERSION_MISMATCH(412),
    VERSION_REQUIRED(428),
    INTERNAL_ERROR(500),
    NOT_SUPPORTED(501),
    UNAVAILABLE(503),
    JSON_PARSE_ERROR(504);

    private final int _value;
    
	private ErrorType(final int value) { 
		_value = value; 
	}
	
    public int getValue() {
        return _value;
    }
	
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("");
		stringBuilder.append(_value);
		return stringBuilder.toString();
	}
}
