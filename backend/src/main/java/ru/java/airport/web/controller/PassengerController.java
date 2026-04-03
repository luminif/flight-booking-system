package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsAuthenticated;
import ru.java.airport.annotations.IsEmployeeOrAdmin;
import ru.java.airport.annotations.IsUserOrAdmin;
import ru.java.airport.service.PassengerService;
import ru.java.airport.utils.Utils;
import ru.java.airport.web.dto.passenger.request.CreatePassengerRequest;
import ru.java.airport.web.dto.passenger.request.UpdatePassengerRequest;
import ru.java.airport.web.dto.passenger.response.GetPassengerResponse;
import ru.java.airport.web.dto.passenger.response.PassengerResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping("/me")
    @IsAuthenticated
    public ResponseEntity<PassengerResponse> createMyPassenger(
        @Valid @RequestBody CreatePassengerRequest request,
        Authentication authentication
    ) {
        Long userId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(request, userId));
    }

    @GetMapping("/me")
    @IsAuthenticated
    public ResponseEntity<PassengerResponse> getMyPassenger(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(passengerService.getPassengerByUserId(userId));
    }

    @GetMapping("/me/tickets")
    @IsAuthenticated
    public ResponseEntity<GetPassengerResponse> getMyPassengerWithTickets(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        PassengerResponse passenger = passengerService.getPassengerByUserId(userId);
        return ResponseEntity.ok(passengerService.getPassengerWithTickets(passenger.id()));
    }

    @PutMapping("/me")
    @IsAuthenticated
    public ResponseEntity<PassengerResponse> updateMyPassenger(
        @Valid @RequestBody UpdatePassengerRequest request,
        Authentication authentication
    ) {
        Long userId = Utils.getCurrentUserId(authentication);
        PassengerResponse passenger = passengerService.getPassengerByUserId(userId);
        return ResponseEntity.ok(passengerService.updatePassenger(passenger.id(), request));
    }

    /*@DeleteMapping("/me")
    @IsAuthenticated
    public ResponseEntity<Void> deleteMyPassenger(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        PassengerResponse passenger = passengerService.getPassengerByUserId(userId);
        passengerService.deletePassenger(passenger.id());
        return ResponseEntity.noContent().build();
    }*/

    @PostMapping("/user/{userId}")
    @IsUserOrAdmin
    public ResponseEntity<PassengerResponse> createPassenger(
        @PathVariable Long userId,
        @Valid @RequestBody CreatePassengerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(request, userId));
    }

    @GetMapping
    @IsEmployeeOrAdmin
    public ResponseEntity<List<PassengerResponse>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/{id}")
    @IsEmployeeOrAdmin
    public ResponseEntity<PassengerResponse> getPassenger(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassenger(id));
    }

    @GetMapping("/{id}/tickets")
    @IsEmployeeOrAdmin
    public ResponseEntity<GetPassengerResponse> getPassengerWithTickets(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerWithTickets(id));
    }

    @GetMapping("/user/{userId}")
    @IsUserOrAdmin
    public ResponseEntity<PassengerResponse> getPassengerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(passengerService.getPassengerByUserId(userId));
    }

    @GetMapping("/search")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<PassengerResponse>> getPassengersByLastName(
        @RequestParam String lastName) {
        return ResponseEntity.ok(passengerService.getPassengersByLastName(lastName));
    }

    @PutMapping("/{id}")
    @IsUserOrAdmin
    public ResponseEntity<PassengerResponse> updatePassenger(
        @PathVariable Long id,
        @Valid @RequestBody UpdatePassengerRequest request) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
