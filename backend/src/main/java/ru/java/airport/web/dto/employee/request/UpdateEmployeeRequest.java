package ru.java.airport.web.dto.employee.request;

public record UpdateEmployeeRequest(
    String firstName,
    String lastName,
    String position,
    String licenseNumber
) {
}
