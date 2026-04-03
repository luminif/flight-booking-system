package ru.java.airport.web.dto.passenger.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePassengerRequest(
    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @NotBlank(message = "Passport number is required")
    @Size(min = 8, max = 20, message = "Passport number must be between {min} and {max} characters")
    String passportNumber,

    @Size(max = 20, message = "Phone number is too long")
    String phone,

    @Size(max = 100, message = "Email is too long")
    String email
) {
}
