package ru.java.airport.web.dto.flight.response;

import ru.java.airport.entity.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;

public record GetFlightResponse(
    Long id,
    String flightNumber,
    Long airlineId,
    String airlineName,
    Long aircraftId,
    String aircraftModel,
    Long departureAirportId,
    String departureAirport,
    String departureCity,
    Long arrivalAirportId,
    String arrivalAirport,
    String arrivalCity,
    LocalDateTime scheduledDeparture,
    LocalDateTime scheduledArrival,
    LocalDateTime actualDeparture,
    LocalDateTime actualArrival,
    FlightStatus status,
    List<TicketBriefResponse> tickets,
    List<CrewBriefResponse> crew
) {
}
