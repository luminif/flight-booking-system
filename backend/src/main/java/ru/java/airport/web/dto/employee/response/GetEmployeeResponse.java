package ru.java.airport.web.dto.employee.response;

import java.util.List;

public record GetEmployeeResponse(
    Long id,
    String firstName,
    String lastName,
    String position,
    String licenseNumber,
    Long userId,
    List<CrewAssignmentResponse> crewAssignments
) {
}
