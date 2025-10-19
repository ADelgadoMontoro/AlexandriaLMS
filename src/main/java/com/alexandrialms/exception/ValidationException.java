package com.alexandrialms.exception;

public class ValidationException extends RuntimeException {
    private String field;
    private String errorCode;

    public ValidationException(String field, String errorCode, String message) {
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getField() { return field; }
    public String getErrorCode() { return errorCode; }
}
