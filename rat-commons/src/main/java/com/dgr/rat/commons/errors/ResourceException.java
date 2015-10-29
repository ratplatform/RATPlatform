/*
 * @author Daniele Grignani
 * Apr 12, 2015
*/

package com.dgr.rat.commons.errors;

import java.util.concurrent.ExecutionException;

public class ResourceException extends ExecutionException{
	private static final long serialVersionUID = 1L;
	private boolean includeCause = false;
    /** The numeric code of the exception. */
    private final ErrorType _code;

    /** The short reason phrase of the exception. */
    private String reason;

    /** Additional detail which can be evaluated by applications. */
//    private JsonValue detail = new JsonValue(null);
    /**
     * The name of the JSON field used for the detail.
     *
     * @see #getDetail()
     * @see #toJsonValue()
     */
    public static final String FIELD_DETAIL = "detail";

    /**
     * The name of the JSON field used for the message.
     *
     * @see #getMessage()
     * @see #toJsonValue()
     */
    public static final String FIELD_MESSAGE = "message";

    /**
     * The name of the JSON field used for the reason.
     *
     * @see #getReason()
     * @see #toJsonValue()
     */
    public static final String FIELD_REASON = "reason";

    /**
     * The name of the JSON field used for the code.
     *
     * @see #getCode()
     * @see #toJsonValue()
     */
    public static final String FIELD_CODE = "code";

    /**
     * The name of the JSON field used for the cause message.
     *
     * @see #getCause()
     * @see #toJsonValue()
     */
    public static final String FIELD_CAUSE = "cause";

    /** flag to indicate whether to include the cause. */
    

    /**
     * Returns this ResourceException with the includeCause flag set to true
     * so that toJsonValue() method will include the cause if there is
     * one supplied.
     *
     * @return  the exception where this flag has been set
     */
    public final ResourceException includeCauseInJsonValue() {
        includeCause = true;
        return this;
    }

    /**
     * Returns an exception with the specified HTTP error code, but no detail
     * message or cause, and a default reason phrase. Useful for translating
     * HTTP status codes to the relevant Java exception type. The type of the
     * returned exception will be a sub-type of {@code ResourceException}.
     *
     * @param code
     *            The HTTP error code.
     * @return A resource exception having the provided HTTP error code.
     */
    public static ResourceException getException(final ErrorType code) {
        return getException(code, null, null);
    }

    /**
     * Returns an exception with the specified HTTP error code and detail
     * message, but no cause, and a default reason phrase. Useful for
     * translating HTTP status codes to the relevant Java exception type. The
     * type of the returned exception will be a sub-type of
     * {@code ResourceException}.
     *
     * @param code
     *            The HTTP error code.
     * @param message
     *            The detail message.
     * @return A resource exception having the provided HTTP error code.
     */
    public static ResourceException getException(final ErrorType code, final String message) {
        return getException(code, message, null);
    }
    
    public static ResourceException getException(final ErrorType code, final Throwable cause) {
        return getException(code, null, cause);
    }

    /**
     * Returns an exception with the specified HTTP error code, detail message,
     * and cause, and a default reason phrase. Useful for translating HTTP
     * status codes to the relevant Java exception type. The type of the
     * returned exception will be a sub-type of {@code ResourceException}.
     *
     * @param code
     *            The HTTP error code.
     * @param message
     *            The detail message.
     * @param cause
     *            The exception which caused this exception to be thrown.
     * @return A resource exception having the provided HTTP error code.
     */
    public static ResourceException getException(final ErrorType code, final String message, final Throwable cause) {
        ResourceException ex = null;
        
        switch (code) {
        case BAD_REQUEST:
            ex = new BadRequestException(message, cause);
            break;
        case FORBIDDEN:
//            ex = new ForbiddenException(message, cause);
            break; 
        case NOT_FOUND:
//            ex = new NotFoundException(message, cause);
            break;
        case CONFLICT:
//            ex = new ConflictException(message, cause);
            break;
        case VERSION_MISMATCH:
//            ex = new PreconditionFailedException(message, cause);
            break;
        case VERSION_REQUIRED:
//            ex = new PreconditionRequiredException(message, cause);
            break; // draft-nottingham-http-new-status-03
        case INTERNAL_ERROR:
//            ex = new InternalServerErrorException(message, cause);
            break;
        case NOT_SUPPORTED:
//            ex = new NotSupportedException(message, cause);
            break; // Not Implemented
        case UNAVAILABLE:
//            ex = new ServiceUnavailableException(message, cause);
            break;
        // Temporary failures without specific exception classes
        case REQUEST_TIME_OUT: // Request Time-out
            break;

        // Permanent Failures without specific exception classes
        case UNAUTOTHORIZED: // Unauthorized - Missing or bad authentication
//            ex = new PermanentException(code, message, cause);
            break;
            
        case JSON_PARSE_ERROR:
        	ex = new JSONParseException(message);
        	break;
        	
        default:
//            ex = new UncategorizedException(code, message, cause);
        }
        
        return ex;
    }

    /**
     * Returns the reason phrase for an HTTP error status code, per RFC 2616 and
     * draft-nottingham-http-new-status-03. If no match is found, then a generic
     * reason {@code "Resource Exception"} is returned.
     */
    private static String reason(final ErrorType code) {
        String result = "Resource Exception"; // default
        switch (code) {
        case BAD_REQUEST:
            result = "Bad Request";
            break;
        case UNAUTOTHORIZED:
            result = "Unauthorized";
            break;
        case FORBIDDEN:
            result = "Forbidden";
            break;
        case NOT_FOUND:
            result = "Not Found";
            break;
        case METHOD_NOT_ALLOWED:
            result = "Method Not Allowed";
            break;
        case REQUEST_TIME_OUT:
            result = "Request Time-out";
            break;
        case CONFLICT:
            result = "Conflict";
            break;
        case VERSION_MISMATCH:
            result = "Precondition Failed";
            break;
        case VERSION_REQUIRED:
            result = "Precondition Required";
            break;
        case INTERNAL_ERROR:
            result = "Internal Server Error";
            break;
        case NOT_SUPPORTED:
            result = "Not Implemented";
            break;
        case UNAVAILABLE:
            result = "Service Unavailable";
            break;
        }
        return result;
    }

    /**
     * Returns the message which should be returned by {@link #getMessage()}.
     */
    private static String message(final ErrorType code, final String message, final Throwable cause) {
        if (message != null) {
            return message;
        } else if (cause != null && cause.getMessage() != null) {
            return cause.getMessage();
        } else {
            return reason(code);
        }
    }

    /**
     * Constructs a new exception with the specified exception code, and
     * {@code null} as its detail message. If the error code corresponds with a
     * known HTTP error status code, then the reason phrase is set to a
     * corresponding reason phrase, otherwise is set to a generic value
     * {@code "Resource Exception"}.
     *
     * @param code
     *            The numeric code of the exception.
     */
    protected ResourceException(final ErrorType code) {
        this(code, null, null);
    }

    /**
     * Constructs a new exception with the specified exception code and detail
     * message.
     *
     * @param code
     *            The numeric code of the exception.
     * @param message
     *            The detail message.
     */
    protected ResourceException(final ErrorType code, final String message) {
        this(code, message, null);
    }

    /**
     * Constructs a new exception with the specified exception code and detail
     * message.
     *
     * @param code
     *            The numeric code of the exception.
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    protected ResourceException(final ErrorType code, final Throwable cause) {
        this(code, null, cause);
    }

    /**
     * Constructs a new exception with the specified exception code, reason
     * phrase, detail message and cause.
     *
     * @param code
     *            The numeric code of the exception.
     * @param message
     *            The detail message.
     * @param cause
     *            The exception which caused this exception to be thrown.
     */
    protected ResourceException(final ErrorType code, final String message, final Throwable cause) {
        super(message(code, message, cause), cause);
        
        this._code = code;
        this.reason = reason(code);
    }

    /**
     * Returns the numeric code of the exception.
     *
     * @return The numeric code of the exception.
     */
    public final ErrorType getCode() {
        return _code;
    }

    /**
     * Returns true if the HTTP error code is in the 500 range.
     *
     * @return <code>true</code> if HTTP error code is in the 500 range.
     */
    public boolean isServerError() {
        return _code.getValue() >= 500 && _code.getValue() <= 599;
    }

    /**
     * Returns the additional detail which can be evaluated by applications. By
     * default there is no additional detail (
     * {@code getDetail().isNull() == true}), and it is the responsibility of
     * the resource provider to add it if needed.
     *
     * @return The additional detail which can be evaluated by applications
     *         (never {@code null}).
     */
    
    // TODO da restituire la stringa json dell'errore
//    public final JsonValue getDetail() {
//        return detail;
//    }

    /**
     * Returns the short reason phrase of the exception.
     *
     * @return The short reason phrase of the exception.
     */
    public final String getReason() {
        return reason;
    }

    /**
     * Sets the additional detail which can be evaluated by applications. By
     * default there is no additional detail (
     * {@code getDetail().isNull() == true}), and it is the responsibility of
     * the resource provider to add it if needed.
     *
     * @param detail
     *            The additional detail which can be evaluated by applications.
     * @return This resource exception.
     */
//    public final ResourceException setDetail(JsonValue detail) {
//        this.detail = detail != null ? detail : new JsonValue(null);
//        return this;
//    }

    /**
     * Sets/overrides the short reason phrase of the exception.
     *
     * @param reason
     *            The short reason phrase of the exception.
     * @return This resource exception.
     */
    public final ResourceException setReason(final String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Returns the exception in a JSON object structure, suitable for inclusion
     * in the entity of an HTTP error response. The JSON representation looks
     * like this:
     *
     * <pre>
     * {
     *     "code"    : 404,
     *     "reason"  : "...",  // optional
     *     "message" : "...",  // required
     *     "detail"  : { ... } // optional
     *     "cause"   : { ... } // optional iff includeCause is set to true
     * }
     * </pre>
     *
     * @return The exception in a JSON object structure, suitable for inclusion
     *         in the entity of an HTTP error response.
     */
    
    // TODO: trasformare l'errore in un json
//    public final JsonValue toJsonValue() {
//        final Map<String, Object> result = new LinkedHashMap<String, Object>(4);
//        result.put(FIELD_CODE, code); // required
//        if (reason != null) { // optional
//            result.put(FIELD_REASON, reason);
//        }
//        final String message = getMessage();
//        if (message != null) { // should always be present
//            result.put(FIELD_MESSAGE, message);
//        }
//        if (!detail.isNull()) {
//            result.put(FIELD_DETAIL, detail.getObject());
//        }
//        if (includeCause && getCause() != null && getCause().getMessage() != null) {
//            final Map<String, Object> cause = new LinkedHashMap<String, Object>(2);
//            cause.put("message", getCause().getMessage());
//            result.put(FIELD_CAUSE, cause);
//        }
//        return new JsonValue(result);
//    }
}
