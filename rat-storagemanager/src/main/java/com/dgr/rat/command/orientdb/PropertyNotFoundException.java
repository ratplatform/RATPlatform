/**
 * @author Daniele Grignani (dgr)
 * @date Jun 12, 2015
 */

package com.dgr.rat.command.orientdb;

import com.dgr.rat.commons.errors.ErrorType;
import com.dgr.rat.commons.errors.ResourceException;

public class PropertyNotFoundException extends ResourceException{
	private static final long serialVersionUID = 8971514893322095189L;
	
	public PropertyNotFoundException() {
		super(ErrorType.NOT_FOUND);
	}
	
    public PropertyNotFoundException(final String message) {
        super(ErrorType.NOT_FOUND, message);
    }

    public PropertyNotFoundException(final String message, final Throwable cause) {
        super(ErrorType.NOT_FOUND, message, cause);
    }

    public PropertyNotFoundException(final Throwable cause) {
        super(ErrorType.NOT_FOUND, cause);
    }
}
