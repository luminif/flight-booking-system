package ru.java.airport.web.dto.auth;

public record JwtResponse(
    Long id,
    String login,
    String accessToken,
    String refreshToken
) {
}