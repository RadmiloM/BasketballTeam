package com.basketball.Basketball.controller;

import com.basketball.Basketball.exception.NoBasketballTeamWithProvidedId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NoBasketballTeamWithProvidedId.class)
    public ResponseEntity<String> handleNoBasketballTeamWithProvidedId(NoBasketballTeamWithProvidedId basketballTeam) {
        return new ResponseEntity<>(basketballTeam.getMessage(), HttpStatus.NOT_FOUND);
    }
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request){
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String basketballField = ((FieldError) error).getField();
            String basketballError = error.getDefaultMessage();
            errors.put(basketballField,basketballError);
        });
        return ResponseEntity.ok(errors);
    }


}
