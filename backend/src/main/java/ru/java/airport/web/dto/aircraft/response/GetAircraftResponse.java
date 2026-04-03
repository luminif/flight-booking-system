package ru.java.airport.web.dto.aircraft.response;

import ru.java.airport.web.dto.airport.response.FlightBriefResponse;

import java.util.List;

public record GetAircraftResponse(
    Long id,
    String model,
    Integer capacity,
    String registrationNumber,
    Long airlineId,
    String airlineName,
    List<FlightBriefResponse> flights
) {
}
