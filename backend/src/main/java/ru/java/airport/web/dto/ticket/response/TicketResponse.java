package ru.java.airport.web.dto.ticket.response;

import ru.java.airport.entity.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketResponse(
    Long id,
    Long flightId,
    String flightNumber,
    Long passengerId,
    String passengerName,
    String seatNumber,
    BigDecimal price,
    LocalDateTime purchaseDate,
    TicketStatus status
) {
}
