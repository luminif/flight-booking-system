package ru.java.airport.web.dto.passenger.request;

public record UpdatePassengerRequest(
    String firstName,
    String lastName,
    String phone,
    String email
) {
}
