package ru.java.airport.web.dto.employee.response;

public record CrewAssignmentResponse(
    Long id,
    Long flightId,
    String flightNumber,
    String roleOnFlight
) {
}
