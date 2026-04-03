package ru.java.airport.web.dto.passenger.response;

import java.util.List;

public record GetPassengerResponse(
    Long id,
    String firstName,
    String lastName,
    String passportNumber,
    String phone,
    String email,
    Long userId,
    List<TicketInfoResponse> tickets
) {
}
