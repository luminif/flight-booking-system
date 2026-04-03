package ru.java.airport.web.dto.flight.response;

import ru.java.airport.entity.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightResponse(
    Long id,
    String flightNumber,
    String airlineName,
    String aircraftModel,
    String departureAirport,
    String arrivalAirport,
    LocalDateTime scheduledDeparture,
    LocalDateTime scheduledArrival,
    FlightStatus status,
    BigDecimal price
) {
}
