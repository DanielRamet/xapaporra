package com.xapaya.xapaporra.porra.api.exception;

public class MessageException extends RuntimeException {
    public MessageException(String text) {
        super("Exception occurred. Message: " + text);
    }
}
