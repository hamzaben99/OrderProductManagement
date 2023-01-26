package com.example.store.utils;

public class NoSuchProductException extends RuntimeException{
    String message;

    public NoSuchProductException(String message) {
        super(message);
        this.message = message;
    }
}
