package ru.java.airport.web.dto.flightcrew.response;

public record FlightCrewResponse(
    Long id,
    Long flightId,
    String flightNumber,
    Long employeeId,
    String employeeName,
    String roleOnFlight
) {
}
