package ru.java.airport.web.dto.user.response;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
    Long id,
    String username,
    LocalDateTime createdAt,
    Set<String> roles
) {
}
