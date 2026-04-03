package ru.java.airport.web.dto.user.response;

public record PassengerInfoResponse(
    Long id,
    String firstName,
    String lastName,
    String passportNumber,
    String phone,
    String email
) {
}
