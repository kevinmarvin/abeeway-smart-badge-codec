package com.github.kevinmarvin.abeeway.exceptions;

/**
 * Exception thrown when downlink message encoding fails.
 */
public class EncodingException extends Exception {

    public EncodingException(String message) {
        super(message);
    }

    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}