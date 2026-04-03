package ru.java.airport.web.dto.airport.response;

import ru.java.airport.entity.FlightStatus;

public record FlightBriefResponse(
    Long id,
    String flightNumber,
    String airline,
    String scheduledDeparture,
    FlightStatus status
) {
}
