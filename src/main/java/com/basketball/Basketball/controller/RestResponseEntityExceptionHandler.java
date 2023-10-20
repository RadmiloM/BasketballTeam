package com.basketball.Basketball.controller;

import com.basketball.Basketball.exception.NoBasketballTeamWithProvidedId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= NoBasketballTeamWithProvidedId.class)
    public ResponseEntity<String> handleNoBasketballTeamWithProvidedId(NoBasketballTeamWithProvidedId basketballTeam){
        return new ResponseEntity<>(basketballTeam.getMessage(), HttpStatus.NOT_FOUND);
    }
}
