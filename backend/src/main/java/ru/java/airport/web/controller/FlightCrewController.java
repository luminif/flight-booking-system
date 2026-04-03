package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsEmployeeOrAdmin;
import ru.java.airport.service.FlightCrewService;
import ru.java.airport.web.dto.flightcrew.request.AssignCrewRequest;
import ru.java.airport.web.dto.flightcrew.response.FlightCrewResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flight-crew")
@RequiredArgsConstructor
public class FlightCrewController {
    private final FlightCrewService flightCrewService;

    @PostMapping
    @IsEmployeeOrAdmin
    public ResponseEntity<FlightCrewResponse> assignCrew(@Valid @RequestBody AssignCrewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightCrewService.assignCrew(request));
    }

    @GetMapping("/flight/{flightId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<FlightCrewResponse>> getCrewByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightCrewService.getCrewByFlight(flightId));
    }

    @GetMapping("/flight/{flightId}/details")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<FlightCrewResponse>> getCrewByFlightWithDetails(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightCrewService.getCrewByFlightWithDetails(flightId));
    }

    @GetMapping("/employee/{employeeId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<FlightCrewResponse>> getCrewByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(flightCrewService.getCrewByEmployee(employeeId));
    }

    @GetMapping("/flight/{flightId}/role")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<FlightCrewResponse>> getCrewByFlightAndRole(
        @PathVariable Long flightId,
        @RequestParam String role
    ) {
        return ResponseEntity.ok(flightCrewService.getCrewByFlightAndRole(flightId, role));
    }

    @PatchMapping("/{id}/role")
    @IsEmployeeOrAdmin
    public ResponseEntity<FlightCrewResponse> updateRole(
        @PathVariable Long id,
        @RequestParam String role
    ) {
        return ResponseEntity.ok(flightCrewService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    @IsEmployeeOrAdmin
    public ResponseEntity<Void> removeCrew(@PathVariable Long id) {
        flightCrewService.removeCrew(id);
        return ResponseEntity.noContent().build();
    }
}
