package ru.java.airport.web.dto.employee.request;

import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeRequest(
    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @NotBlank(message = "Position is required")
    String position,

    String licenseNumber
) {
}
