package com.example.challenge.exceptions;

public class InvalidCreditValueException extends RuntimeException {
    public InvalidCreditValueException(String message) {
        super(message);
    }
}