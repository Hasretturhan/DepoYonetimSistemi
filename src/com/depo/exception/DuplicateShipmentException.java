package com.depo.exception;

public class DuplicateShipmentException extends RuntimeException {
    public DuplicateShipmentException(String message) {
        super(message);
    }
}
