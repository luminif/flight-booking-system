package ru.java.airport.web.dto.airline.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAirlineRequest(
    @NotBlank(message = "Airline name is required")
    String name,

    String country,

    @Size(min = 2, max = 3, message = "IATA code must be 2 or 3 characters")
    String iataCode
) {
}
