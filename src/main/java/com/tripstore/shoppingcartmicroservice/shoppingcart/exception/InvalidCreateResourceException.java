package com.tripstore.shoppingcartmicroservice.shoppingcart.exception;

public class InvalidCreateResourceException extends RuntimeException {
    public InvalidCreateResourceException() {

    }
    public InvalidCreateResourceException(String message) {
        super(message);
    }
}
