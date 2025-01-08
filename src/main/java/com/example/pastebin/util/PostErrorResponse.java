package com.example.pastebin.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostErrorResponse {
    private String message;
    private long timestamp;

    public PostErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
