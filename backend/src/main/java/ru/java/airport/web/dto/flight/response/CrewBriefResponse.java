package ru.java.airport.web.dto.flight.response;

public record CrewBriefResponse(
    Long id,
    Long employeeId,
    String employeeName,
    String roleOnFlight
) {
}
