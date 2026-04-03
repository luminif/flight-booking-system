package ru.java.airport.web.dto.airline.request;

public record UpdateAirlineRequest(
    String name,
    String country,
    String iataCode
) {
}
