package com.aacdemy.moonlight.exception;

public class AdminDeleteException extends RuntimeException {
    public AdminDeleteException() {
        super("Deleting administrators is not allowed.");
    }
}