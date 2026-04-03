package ru.java.airport.web.dto.flight.response;

import ru.java.airport.entity.TicketStatus;

public record TicketBriefResponse(
    Long id,
    String seatNumber,
    String price,
    String passengerName,
    TicketStatus status
) {
}
