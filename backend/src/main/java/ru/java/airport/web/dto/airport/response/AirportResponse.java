package ru.java.airport.web.dto.airport.response;

public record AirportResponse(
    Long id,
    String name,
    String city,
    String country,
    String iataCode
) {
}
