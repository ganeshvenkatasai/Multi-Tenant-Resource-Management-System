package com.example.multitenant.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String msg) { super(msg); }
}
