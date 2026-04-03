package ru.java.airport.web.dto.statistics;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PassengerStatistics(
    Long passengerId,
    String passengerName,
    Long ticketsCount,
    BigDecimal totalSpent,
    LocalDateTime firstFlight,
    LocalDateTime lastFlight
) {
}
