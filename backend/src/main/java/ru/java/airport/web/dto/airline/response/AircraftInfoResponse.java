package ru.java.airport.web.dto.airline.response;

public record AircraftInfoResponse(
    Long id,
    String model,
    Integer capacity,
    String registrationNumber
) {
}
