package ru.java.airport.web.dto.aircraft.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateAircraftRequest(
    @NotBlank(message = "Model is required")
    String model,

    @Min(value = 1, message = "Capacity must be at least 1")
    Integer capacity,

    Long airlineId,

    String registrationNumber
) {
}
