package com.kfc.app.exception;

import com.kfc.app.service.impl.UserServiceImpl;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> NotFoundExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Not Found", ex.getMessage());
        return new ResponseEntity<Map<String, String>>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        log.info(ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Internal error", "Please contact support");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> validationExceptions(Exception ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<Map<String, String>> duplicatedExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Duplicated User", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}