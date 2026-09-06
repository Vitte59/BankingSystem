package com.bank.exception;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
