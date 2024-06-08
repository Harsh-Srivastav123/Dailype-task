package com.dailype.dailypetask.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ResponseStatus
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) throws Exception {
        HashMap<String,Object> error=new HashMap<>();
        if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
            log.info("error handle here !!");
            error.put("httpStatus", HttpStatus.BAD_REQUEST);
            error.put("message",ex.getMessage());
            throw  ex;
        }else {
            return null;
        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body((error);
        // Handle other exceptions
        // ...
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.info("error  "+ex.getMessage());
        HashMap<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("httpStatus",HttpStatus.BAD_REQUEST);
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> customException(NotFoundException exception) {


        HashMap<String,Object> error=new HashMap<>();
        error.put("httpStatus",HttpStatus.NOT_FOUND);
        error.put("message",exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> customException(BadRequestException exception) {


        HashMap<String,Object> error=new HashMap<>();
        error.put("httpStatus",HttpStatus.BAD_REQUEST);
        error.put("message",exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
