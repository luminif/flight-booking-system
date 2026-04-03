package ru.java.airport.web.dto.flight.request;

import ru.java.airport.entity.FlightStatus;

import java.time.LocalDateTime;

public record UpdateFlightRequest(
    LocalDateTime scheduledDeparture,
    LocalDateTime scheduledArrival,
    FlightStatus status
) {
}
