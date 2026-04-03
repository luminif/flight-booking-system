package ru.java.airport.web.dto.statistics;

import java.math.BigDecimal;

public record AirlineStatistics(
    Long airlineId,
    String airlineName,
    Long totalFlights,
    Long totalAircrafts,
    Long totalPassengers,
    BigDecimal totalRevenue
) {
}
