package org.adaschool.api.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}