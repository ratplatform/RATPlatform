/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.storage.orientdb;

import com.dgr.rat.commons.errors.ErrorType;
import com.dgr.rat.commons.errors.ResourceException;

// TODO: non va bene StatusCode.InternalServerError, da rivedere
public class StorageInternalError extends ResourceException{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     */
    public StorageInternalError() {
        super(ErrorType.INTERNAL_ERROR);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message
     *            The detail message.
     */
    public StorageInternalError(final String message) {
        super(ErrorType.INTERNAL_ERROR, message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message
     *            The detail message.
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public StorageInternalError(final String message, final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public StorageInternalError(final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, cause);
    }

}
