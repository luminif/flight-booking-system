package ru.java.airport.web.dto.airline.response;

import ru.java.airport.entity.FlightStatus;

public record FlightInfoResponse(
    Long id,
    String flightNumber,
    String departureAirport,
    String arrivalAirport,
    FlightStatus status
) {
}
