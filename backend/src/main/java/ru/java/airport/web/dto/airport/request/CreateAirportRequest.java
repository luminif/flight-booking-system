package ru.java.airport.web.dto.airport.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAirportRequest(
    @NotBlank(message = "Airport name is required")
    String name,

    String city,

    String country,

    @Size(min = 2, max = 3, message = "IATA code must be 2 or 3 characters")
    String iataCode
) {
}
