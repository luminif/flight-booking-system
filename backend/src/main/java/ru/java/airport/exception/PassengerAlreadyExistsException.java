package ru.java.airport.exception;

public class PassengerAlreadyExistsException extends RuntimeException {
    public PassengerAlreadyExistsException(String message) {
        super(message);
    }
}
