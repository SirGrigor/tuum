package com.ilgrig.tuum.util;

public class BalanceNotFoundException extends RuntimeException {
    public BalanceNotFoundException(String message) {
        super(message);
    }
}
