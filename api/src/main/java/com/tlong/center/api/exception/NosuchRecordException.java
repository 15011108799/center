package com.tlong.center.api.exception;

public class NosuchRecordException extends Exception {
    public NosuchRecordException() {
    }

    public NosuchRecordException(String message) {
        super(message);
    }
}
