package ru.java.airport.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between {min} and {max} characters")
    String password,

    Set<String> roles
) {
}
