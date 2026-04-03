package ru.java.airport.web.dto.airline.response;

import java.util.List;

public record GetAirlineResponse(
    Long id,
    String name,
    String country,
    String iataCode,
    List<AircraftInfoResponse> aircrafts,
    List<FlightInfoResponse> flights
) {
}
