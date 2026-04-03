package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.service.AirportService;
import ru.java.airport.web.dto.airport.request.CreateAirportRequest;
import ru.java.airport.web.dto.airport.request.UpdateAirportRequest;
import ru.java.airport.web.dto.airport.response.AirportResponse;
import ru.java.airport.web.dto.airport.response.GetAirportResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    @IsAdmin
    public ResponseEntity<AirportResponse> createAirport(@Valid @RequestBody CreateAirportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(airportService.createAirport(request));
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirport(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirport(id));
    }

    @GetMapping("/{id}/flights")
    public ResponseEntity<GetAirportResponse> getAirportWithFlights(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportWithFlights(id));
    }

    @GetMapping("/search/by-city")
    public ResponseEntity<List<AirportResponse>> getAirportsByCity(@RequestParam String city) {
        return ResponseEntity.ok(airportService.getAirportsByCity(city));
    }

    @GetMapping("/search/by-country")
    public ResponseEntity<List<AirportResponse>> getAirportsByCountry(@RequestParam String country) {
        return ResponseEntity.ok(airportService.getAirportsByCountry(country));
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<AirportResponse> updateAirport(
        @PathVariable Long id,
        @Valid @RequestBody UpdateAirportRequest request
    ) {
        return ResponseEntity.ok(airportService.updateAirport(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
