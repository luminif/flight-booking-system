package ru.java.airport.web.dto.flight.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateFlightRequest(
    @NotBlank(message = "Flight number is required")
    String flightNumber,

    @NotNull(message = "Airline ID is required")
    Long airlineId,

    @NotNull(message = "Aircraft ID is required")
    Long aircraftId,

    @NotNull(message = "Departure airport ID is required")
    Long departureAirportId,

    @NotNull(message = "Arrival airport ID is required")
    Long arrivalAirportId,

    @NotNull(message = "Scheduled departure is required")
    LocalDateTime scheduledDeparture,

    @NotNull(message = "Scheduled arrival is required")
    LocalDateTime scheduledArrival
) {
}
