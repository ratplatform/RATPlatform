/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

public class InternalServerErrorException extends ResourceException{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     */
    public InternalServerErrorException() {
        super(ErrorType.INTERNAL_ERROR);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message
     *            The detail message.
     */
    public InternalServerErrorException(final String message) {
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
    public InternalServerErrorException(final String message, final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public InternalServerErrorException(final Throwable cause) {
        super(ErrorType.INTERNAL_ERROR, cause);
    }
}
