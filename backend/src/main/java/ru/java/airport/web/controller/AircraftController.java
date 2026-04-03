package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.service.AircraftService;
import ru.java.airport.web.dto.aircraft.request.CreateAircraftRequest;
import ru.java.airport.web.dto.aircraft.request.UpdateAircraftRequest;
import ru.java.airport.web.dto.aircraft.response.AircraftResponse;
import ru.java.airport.web.dto.aircraft.response.GetAircraftResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aircrafts")
@RequiredArgsConstructor
public class AircraftController {
    private final AircraftService aircraftService;

    @PostMapping
    @IsAdmin
    public ResponseEntity<AircraftResponse> createAircraft(@Valid @RequestBody CreateAircraftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraftService.createAircraft(request));
    }

    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAllAircrafts() {
        return ResponseEntity.ok(aircraftService.getAllAircrafts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getAircraft(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getAircraft(id));
    }

    @GetMapping("/{id}/flights")
    public ResponseEntity<GetAircraftResponse> getAircraftWithFlights(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getAircraftWithFlights(id));
    }

    @GetMapping("/search/by-airline")
    public ResponseEntity<List<AircraftResponse>> getAircraftsByAirline(@RequestParam Long airlineId) {
        return ResponseEntity.ok(aircraftService.getAircraftsByAirline(airlineId));
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<AircraftResponse> updateAircraft(
        @PathVariable Long id,
        @Valid @RequestBody UpdateAircraftRequest request
    ) {
        return ResponseEntity.ok(aircraftService.updateAircraft(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }
}
