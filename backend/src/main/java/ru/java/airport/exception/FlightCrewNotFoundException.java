package ru.java.airport.exception;

public class FlightCrewNotFoundException extends RuntimeException {
    public FlightCrewNotFoundException(String message) {
        super(message);
    }
}
