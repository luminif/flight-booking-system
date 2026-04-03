package ru.java.airport.web.dto.airport.response;

import java.util.List;

public record GetAirportResponse(
    Long id,
    String name,
    String city,
    String country,
    String iataCode,
    List<FlightBriefResponse> departingFlights,
    List<FlightBriefResponse> arrivingFlights
) {
}
