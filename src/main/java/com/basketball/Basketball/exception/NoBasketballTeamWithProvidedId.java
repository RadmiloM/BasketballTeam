package com.basketball.Basketball.exception;

public class NoBasketballTeamWithProvidedId extends RuntimeException {

    public NoBasketballTeamWithProvidedId(String message) {
        super(message);
    }

}
