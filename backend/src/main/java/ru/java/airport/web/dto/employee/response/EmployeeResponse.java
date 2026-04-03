package ru.java.airport.web.dto.employee.response;

public record EmployeeResponse(
    Long id,
    String firstName,
    String lastName,
    String position,
    String licenseNumber,
    Long userId
) {
}
