package com.techpixe.exception;

import org.springframework.http.HttpStatus;

public class CustomStatusException extends RuntimeException 
{
    private final int statusCode;

    public CustomStatusException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

