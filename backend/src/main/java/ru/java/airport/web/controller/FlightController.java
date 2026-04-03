package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsAuthenticated;
import ru.java.airport.annotations.IsEmployeeOrAdmin;
import ru.java.airport.entity.FlightStatus;
import ru.java.airport.service.FlightService;
import ru.java.airport.web.dto.flight.request.CreateFlightRequest;
import ru.java.airport.web.dto.flight.request.UpdateFlightRequest;
import ru.java.airport.web.dto.flight.request.UpdateFlightStatusRequest;
import ru.java.airport.web.dto.flight.response.FlightResponse;
import ru.java.airport.web.dto.flight.response.GetFlightResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    @IsEmployeeOrAdmin
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody CreateFlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(request));
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlight(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @GetMapping("/{id}/details")
    @IsEmployeeOrAdmin
    public ResponseEntity<GetFlightResponse> getFlightWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightWithDetails(id));
    }

    @GetMapping("/search/by-status")
    public ResponseEntity<List<FlightResponse>> getFlightsByStatus(@RequestParam FlightStatus status) {
        return ResponseEntity.ok(flightService.getFlightsByStatus(status));
    }

    @GetMapping("/search/by-airline")
    public ResponseEntity<List<FlightResponse>> getFlightsByAirline(@RequestParam Long airlineId) {
        return ResponseEntity.ok(flightService.getFlightsByAirline(airlineId));
    }

    @GetMapping("/search/by-route")
    public ResponseEntity<List<FlightResponse>> getFlightsByRoute(
        @RequestParam Long departureAirportId,
        @RequestParam Long arrivalAirportId
    ) {
        return ResponseEntity.ok(flightService.getFlightsByRoute(departureAirportId, arrivalAirportId));
    }

    @GetMapping("/search/by-date-range")
    public ResponseEntity<List<FlightResponse>> getFlightsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(flightService.getFlightsByDateRange(start, end));
    }

    @PutMapping("/{id}")
    @IsEmployeeOrAdmin
    public ResponseEntity<FlightResponse> updateFlight(
        @PathVariable Long id,
        @Valid @RequestBody UpdateFlightRequest request
    ) {
        return ResponseEntity.ok(flightService.updateFlight(id, request));
    }

    @PatchMapping("/{id}/status")
    @IsEmployeeOrAdmin
    public ResponseEntity<FlightResponse> updateFlightStatus(
        @PathVariable Long id,
        @Valid @RequestBody UpdateFlightStatusRequest request
    ) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
