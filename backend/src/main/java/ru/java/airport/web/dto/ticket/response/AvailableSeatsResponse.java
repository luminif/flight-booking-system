package ru.java.airport.web.dto.ticket.response;

import java.util.List;

public record AvailableSeatsResponse(
    Long flightId,
    String flightNumber,
    Integer totalSeats,
    Integer occupiedSeats,
    Integer availableSeats,
    List<String> availableSeatNumbers,
    List<String> occupiedSeatNumbers
) {
}
