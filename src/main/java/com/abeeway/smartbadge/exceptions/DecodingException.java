package com.abeeway.smartbadge.exceptions;

/**
 * Exception thrown when uplink message decoding fails.
 */
public class DecodingException extends Exception {

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}