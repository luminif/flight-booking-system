package ru.java.airport.web.dto.aircraft.response;

public record AircraftResponse(
    Long id,
    String model,
    Integer capacity,
    String registrationNumber,
    Long airlineId,
    String airlineName
) {
}
