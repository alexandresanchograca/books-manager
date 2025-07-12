package com.alexandre.books_manager.exception;

import java.time.Instant;
import java.util.Date;

public class ErrorResponse {
    private String message;
    private Instant timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return this.timestamp.getEpochSecond();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
