package com.springboot.atm.exception;

import org.springframework.http.HttpStatus;

public class AtmAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public AtmAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public AtmAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
