/**
 * @author Daniele Grignani (dgr)
 * @date Jun 12, 2015
 */

package com.dgr.rat.command.orientdb;

import com.dgr.rat.commons.errors.ErrorType;
import com.dgr.rat.commons.errors.ResourceException;

public class TooManyPropertiesException extends ResourceException{
	private static final long serialVersionUID = 8971514893322095189L;
	
	public TooManyPropertiesException() {
		super(ErrorType.INTERNAL_ERROR);
	}
	
    public TooManyPropertiesException(final String message) {
        super(ErrorType.INTERNAL_ERROR, message);
    }

    public TooManyPropertiesException(final String message, final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, message, cause);
    }

    public TooManyPropertiesException(final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, cause);
    }
}
