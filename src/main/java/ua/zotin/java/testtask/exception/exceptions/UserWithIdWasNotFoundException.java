package ua.zotin.java.testtask.exception.exceptions;

public class UserWithIdWasNotFoundException extends RuntimeException {
    public UserWithIdWasNotFoundException(String message) {
        super(message);
    }
}
