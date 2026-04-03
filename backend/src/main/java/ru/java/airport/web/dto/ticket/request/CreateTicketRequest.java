package ru.java.airport.web.dto.ticket.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(
    @NotNull(message = "Flight ID is required")
    Long flightId,

    @NotBlank(message = "Seat number is required")
    String seatNumber
) {
}
