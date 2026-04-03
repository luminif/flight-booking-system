package ru.java.airport.web.dto.airline.response;

public record AirlineResponse(
    Long id,
    String name,
    String country,
    String iataCode
) {
}
