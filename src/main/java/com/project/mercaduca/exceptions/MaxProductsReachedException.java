package com.project.mercaduca.exceptions;

public class MaxProductsReachedException extends RuntimeException {
    public MaxProductsReachedException(String message) {
        super(message);
    }
}