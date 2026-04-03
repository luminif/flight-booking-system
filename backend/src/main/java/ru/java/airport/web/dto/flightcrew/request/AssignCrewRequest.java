package ru.java.airport.web.dto.flightcrew.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignCrewRequest(
    @NotNull(message = "Flight ID is required")
    Long flightId,

    @NotNull(message = "Employee ID is required")
    Long employeeId,

    @NotBlank(message = "Role on flight is required")
    String roleOnFlight
) {
}
