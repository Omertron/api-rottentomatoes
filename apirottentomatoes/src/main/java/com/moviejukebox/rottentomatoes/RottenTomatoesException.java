package com.moviejukebox.rottentomatoes;

public class RottenTomatoesException extends Exception {

    private static final long serialVersionUID = -8952129102483143278L;

    public enum RottenTomatoesExceptionType {

        UNKNOWN_CAUSE, INVALID_URL, HTTP_404_ERROR, MAPPING_FAILED, CONNECTION_ERROR, NO_API_KEY;
    }
    private final RottenTomatoesExceptionType exceptionType;
    private final String response;

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final String response) {
        super(response);
        this.exceptionType = exceptionType;
        this.response = response;
    }

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final String response, final Throwable cause) {
        super(cause);
        this.exceptionType = exceptionType;
        this.response = response;
    }

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final Throwable cause) {
        super(cause);
        this.exceptionType = exceptionType;
        this.response = cause.getMessage();
    }

    public RottenTomatoesExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getResponse() {
        return response;
    }
}
