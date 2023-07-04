package com.aacdemy.moonlight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


public class RoomReservationNotFoundException extends ResponseStatusException {

    public RoomReservationNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public RoomReservationNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
    public RoomReservationNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
