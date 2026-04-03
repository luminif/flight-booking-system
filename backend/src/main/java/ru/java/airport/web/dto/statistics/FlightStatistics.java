package ru.java.airport.web.dto.statistics;

import java.math.BigDecimal;

public record FlightStatistics(
    Long flightId,
    String flightNumber,
    Long totalSeats,
    Long occupiedSeats,
    Long availableSeats,
    BigDecimal occupancyRate,
    BigDecimal totalRevenue
) {
}
