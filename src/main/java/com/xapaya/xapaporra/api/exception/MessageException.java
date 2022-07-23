package com.xapaya.olivapp.jobs.controller.exception;

public class MessageException extends RuntimeException {
    public MessageException(String text) {
        super("Exception occurred. Message: " + text);
    }
}
