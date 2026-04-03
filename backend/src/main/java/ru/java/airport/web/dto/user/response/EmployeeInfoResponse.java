package ru.java.airport.web.dto.user.response;

public record EmployeeInfoResponse(
    Long id,
    String firstName,
    String lastName,
    String position,
    String licenseNumber
) {
}
