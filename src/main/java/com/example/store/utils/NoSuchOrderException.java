package com.example.store.utils;

public class NoSuchOrderException extends RuntimeException{

    public NoSuchOrderException(String message) {
        super(message);
    }
}
