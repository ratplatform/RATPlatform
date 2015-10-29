package com.dgr.rat.commons.constants;

public enum StatusCode {
	Unknown(1),
	Ok(200),
	BadRequest(400),
	Unauthorized(401),
	Forbidden(403),
	NotFound(404),
	MethodNotAllowed(405),
	NotAcceptable(406),
	RequestTimeout(408),
	Conflict(409),
	InternalServerError(500),
	NotImplemented(501),
	ServiceUnavailable(503),
	VersionNotSupported(505);
	
	private int _value = -1;
    private StatusCode(int value) {
        this._value = value;
    }
    
    public int getValue(){
    	return _value;
    }
    
    @Override
    public String toString(){
    	return String.valueOf(_value);
    }
    
    public static StatusCode fromString(String value){
    	StatusCode result = StatusCode.Unknown;
    	
    	try {
			if (value != null) {
				int tmp = Integer.parseInt(value);
				for(StatusCode statusCode : StatusCode.values()) {
					if(tmp == statusCode.getValue()){
						result = statusCode;
						break;
					}
				}
			}
    	} 
    	catch (Exception e) {
    		result = StatusCode.Unknown;
    	}
    	
    	return result;
    }
}
