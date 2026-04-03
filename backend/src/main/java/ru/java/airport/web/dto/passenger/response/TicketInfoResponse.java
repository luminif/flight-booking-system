package ru.java.airport.web.dto.passenger.response;

import ru.java.airport.entity.TicketStatus;

import java.time.LocalDateTime;

public record TicketInfoResponse(
    Long id,
    String flightNumber,
    String seatNumber,
    String price,
    TicketStatus status,
    LocalDateTime purchaseDate
) {
}
