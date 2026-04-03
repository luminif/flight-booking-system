package ru.java.airport.web.dto.airport.request;

public record UpdateAirportRequest(
    String name,
    String city,
    String country,
    String iataCode
) {
}
