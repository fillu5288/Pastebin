package com.example.pastebin.util;

public class PostNotCreated extends RuntimeException {
    public PostNotCreated(String msg) {
        super(msg);
    }
}
