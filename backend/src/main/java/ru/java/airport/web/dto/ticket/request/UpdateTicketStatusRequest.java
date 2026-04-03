package ru.java.airport.web.dto.ticket.request;

import jakarta.validation.constraints.NotNull;
import ru.java.airport.entity.TicketStatus;

public record UpdateTicketStatusRequest(
    @NotNull(message = "Status is required")
    TicketStatus status
) {
}
