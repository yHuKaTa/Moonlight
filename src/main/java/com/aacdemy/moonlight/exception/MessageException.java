package com.aacdemy.moonlight.exception;

/**
 * This class represents an exception that is used to throw a simple message.
 */
public class MessageException extends RuntimeException {

    public MessageException(String message) {
        super(message);
    }
}
