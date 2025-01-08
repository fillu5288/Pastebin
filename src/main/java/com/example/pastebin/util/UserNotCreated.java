package com.example.pastebin.util;

public class UserNotCreated extends RuntimeException {
    public UserNotCreated(String msg) {
        super(msg);
    }
}
