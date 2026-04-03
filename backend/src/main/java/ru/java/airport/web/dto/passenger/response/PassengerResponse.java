package ru.java.airport.web.dto.passenger.response;

public record PassengerResponse(
    Long id,
    String firstName,
    String lastName,
    String passportNumber,
    String phone,
    String email,
    Long userId
) {
}
