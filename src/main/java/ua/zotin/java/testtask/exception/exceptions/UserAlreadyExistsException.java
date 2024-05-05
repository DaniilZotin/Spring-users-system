package ua.zotin.java.testtask.exception.exceptions;

public class UserAlreadyExistsException  extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
