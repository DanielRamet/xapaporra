package com.xapaya.xapaporra.porra.api;

import com.xapaya.xapaporra.porra.api.exception.ErrorMessage;
import com.xapaya.xapaporra.porra.api.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(MessageException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorMessage handleCommentMessageSendException(MessageException ex) {
        log.error("ERROR...", ex);
        return new ErrorMessage(ex.getMessage());
    }
}
