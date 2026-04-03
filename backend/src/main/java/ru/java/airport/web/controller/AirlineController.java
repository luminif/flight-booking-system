package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsAuthenticated;
import ru.java.airport.service.AirlineService;
import ru.java.airport.web.dto.airline.request.CreateAirlineRequest;
import ru.java.airport.web.dto.airline.request.UpdateAirlineRequest;
import ru.java.airport.web.dto.airline.response.AirlineResponse;
import ru.java.airport.web.dto.airline.response.GetAirlineResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;

    @PostMapping
    @IsAdmin
    public ResponseEntity<AirlineResponse> createAirline(@Valid @RequestBody CreateAirlineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(airlineService.createAirline(request));
    }

    @GetMapping
    public ResponseEntity<List<AirlineResponse>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> getAirline(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getAirline(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GetAirlineResponse> getAirlineWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getAirlineWithDetails(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AirlineResponse>> getAirlinesByCountry(@RequestParam String country) {
        return ResponseEntity.ok(airlineService.getAirlinesByCountry(country));
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<AirlineResponse> updateAirline(
        @PathVariable Long id,
        @Valid @RequestBody UpdateAirlineRequest request
    ) {
        return ResponseEntity.ok(airlineService.updateAirline(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
