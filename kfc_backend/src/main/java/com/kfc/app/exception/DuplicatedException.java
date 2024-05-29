package com.kfc.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicatedException extends RuntimeException {

    public DuplicatedException(String message) {
        super(message);
    }
}