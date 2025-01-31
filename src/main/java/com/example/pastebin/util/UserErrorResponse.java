package com.example.pastebin.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserErrorResponse {
    private String message;
    private long timestamp;

    public UserErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
