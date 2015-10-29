/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

public class VertexDoesNotExistsException extends ResourceException{
    private static final long serialVersionUID = 1L;

    public VertexDoesNotExistsException() {
        super(ErrorType.NOT_FOUND);
    }

    public VertexDoesNotExistsException(final String message) {
        super(ErrorType.NOT_FOUND, message);
    }

    public VertexDoesNotExistsException(final String message, final Throwable cause) {
        super(ErrorType.NOT_FOUND, message, cause);
    }

    public VertexDoesNotExistsException(final Throwable cause) {
        super(ErrorType.NOT_FOUND, cause);
    }

}
