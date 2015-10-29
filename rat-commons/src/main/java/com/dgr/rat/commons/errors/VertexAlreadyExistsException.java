/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

public class VertexAlreadyExistsException extends ResourceException{
    private static final long serialVersionUID = 1L;

    public VertexAlreadyExistsException() {
        super(ErrorType.CONFLICT);
    }

    public VertexAlreadyExistsException(final String message) {
        super(ErrorType.CONFLICT, message);
    }

    public VertexAlreadyExistsException(final String message, final Throwable cause) {
        super(ErrorType.CONFLICT, message, cause);
    }

    public VertexAlreadyExistsException(final Throwable cause) {
        super(ErrorType.CONFLICT, cause);
    }

}
