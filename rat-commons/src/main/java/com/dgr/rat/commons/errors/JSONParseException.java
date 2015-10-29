/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

public class JSONParseException extends ResourceException{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     */
    public JSONParseException() {
        super(ErrorType.JSON_PARSE_ERROR);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message
     *            The detail message.
     */
    public JSONParseException(final String message) {
        super(ErrorType.JSON_PARSE_ERROR, message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message
     *            The detail message.
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public JSONParseException(final String message, final Throwable cause) {
        super(ErrorType.JSON_PARSE_ERROR, message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    public JSONParseException(final Throwable cause) {
        super(ErrorType.JSON_PARSE_ERROR, cause);
    }
}
