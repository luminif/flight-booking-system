package ru.java.airport.web.dto.aircraft.request;

public record UpdateAircraftRequest(
    String model,
    Integer capacity,
    String registrationNumber
) {
}
