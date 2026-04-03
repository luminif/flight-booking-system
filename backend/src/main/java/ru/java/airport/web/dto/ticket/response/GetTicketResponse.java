package ru.java.airport.web.dto.ticket.response;

import ru.java.airport.entity.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetTicketResponse(
    Long id,
    Long flightId,
    String flightNumber,
    String departureAirport,
    String arrivalAirport,
    LocalDateTime scheduledDeparture,
    Long passengerId,
    String passengerName,
    String passportNumber,
    String seatNumber,
    BigDecimal price,
    LocalDateTime purchaseDate,
    TicketStatus status
) {
}
