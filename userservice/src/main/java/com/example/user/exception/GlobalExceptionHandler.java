package com.example.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException ex){
        LOGGER.error(ex.getMessage(),ex);
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error-> {
                    errorMap.put(error.getField(),error.getDefaultMessage());
                }
        );
        return errorMap;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleUserNotFoundException(UserNotFoundException ex){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("error msg : ",ex.getMessage());
        return errorMap;
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleUserGroupNotFoundException(UserGroupNotFoundException ex){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("error msg : ",ex.getMessage());
        return errorMap;
    }

}