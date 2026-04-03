package ru.java.airport.web.dto.flight.request;

import jakarta.validation.constraints.NotNull;
import ru.java.airport.entity.FlightStatus;

public record UpdateFlightStatusRequest(
    @NotNull(message = "Status is required")
    FlightStatus status
) {
}
