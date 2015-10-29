/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

public class BadRequestException extends ResourceException{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     */
    public BadRequestException() {
        super(ErrorType.BAD_REQUEST);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message
     *            The detail message.
     */
    public BadRequestException(final String message) {
        super(ErrorType.BAD_REQUEST, message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message
     *            The detail message.
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public BadRequestException(final String message, final Throwable cause) {
        super(ErrorType.BAD_REQUEST, message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public BadRequestException(final Throwable cause) {
        super(ErrorType.BAD_REQUEST, cause);
    }
}
