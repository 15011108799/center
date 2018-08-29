package com.tlong.center.api.exception;

public class NosuchRecordException extends RuntimeException {
    public NosuchRecordException() {
    }

    public NosuchRecordException(String message) {
        super(message);
    }
}
