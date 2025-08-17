package com.example.multitenant.exception;

public class TenantAlreadyExistsException extends RuntimeException {
    public TenantAlreadyExistsException(String msg) { super(msg); }
}
