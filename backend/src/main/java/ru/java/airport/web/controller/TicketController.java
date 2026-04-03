package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAuthenticated;
import ru.java.airport.annotations.IsEmployeeOrAdmin;
import ru.java.airport.entity.TicketStatus;
import ru.java.airport.service.FlightService;
import ru.java.airport.service.PassengerService;
import ru.java.airport.service.TicketService;
import ru.java.airport.utils.Utils;
import ru.java.airport.web.dto.flight.response.FlightResponse;
import ru.java.airport.web.dto.passenger.response.PassengerResponse;
import ru.java.airport.web.dto.ticket.request.CreateTicketRequest;
import ru.java.airport.web.dto.ticket.request.UpdateTicketStatusRequest;
import ru.java.airport.web.dto.ticket.response.AvailableSeatsResponse;
import ru.java.airport.web.dto.ticket.response.GetTicketResponse;
import ru.java.airport.web.dto.ticket.response.TicketResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final PassengerService passengerService;
    private final FlightService flightService;

    @PostMapping("/me")
    @IsAuthenticated
    public ResponseEntity<TicketResponse> buyTicket(
        @Valid @RequestBody CreateTicketRequest request,
        Authentication authentication
    ) {
        Long userId = Utils.getCurrentUserId(authentication);
        PassengerResponse passenger = passengerService.getPassengerByUserId(userId);

        FlightResponse flight = flightService.getFlight(request.flightId());

        TicketResponse response = ticketService.createTicket(
            request.flightId(),
            passenger.id(),
            request.seatNumber(),
            flight.price()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @IsAuthenticated
    public ResponseEntity<List<TicketResponse>> getMyTickets(Authentication authentication) {
        Long currentUserId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getTicketsByUserId(currentUserId));
    }

    @GetMapping("/me/total-spent")
    @IsAuthenticated
    public ResponseEntity<BigDecimal> getMyTotalSpent(Authentication authentication) {
        Long currentUserId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getTotalSpentByUserId(currentUserId));
    }

    @DeleteMapping("/me/{ticketId}/cancel")
    @IsAuthenticated
    public ResponseEntity<Void> cancelMyTicket(
        @PathVariable Long ticketId,
        Authentication authentication
    ) {
        Long currentUserId = Utils.getCurrentUserId(authentication);
        ticketService.cancelTicketByUser(ticketId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/{ticketId}/details")
    @IsAuthenticated
    public ResponseEntity<GetTicketResponse> getMyTicketWithDetails(
        @PathVariable Long ticketId,
        Authentication authentication
    ) {
        Long currentUserId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getMyTicketWithDetails(ticketId, currentUserId));
    }

    @GetMapping("/me/search/by-status")
    @IsAuthenticated
    public ResponseEntity<List<TicketResponse>> getMyTicketsByStatus(
        @RequestParam TicketStatus status,
        Authentication authentication
    ) {
        Long currentUserId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getMyTicketsByStatus(currentUserId, status));
    }

    @GetMapping("/{id}")
    @IsEmployeeOrAdmin
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @GetMapping("/{id}/details")
    @IsEmployeeOrAdmin
    public ResponseEntity<GetTicketResponse> getTicketWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketWithDetails(id));
    }

    @GetMapping("/flight/{flightId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<TicketResponse>> getTicketsByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(ticketService.getTicketsByFlight(flightId));
    }

    @GetMapping("/flight/{flightId}/occupied-seats")
    public ResponseEntity<Long> getOccupiedSeatsCount(@PathVariable Long flightId) {
        return ResponseEntity.ok(ticketService.getOccupiedSeatsCount(flightId));
    }

    @GetMapping("/search/by-status")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping
    @IsEmployeeOrAdmin
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/passenger/{passengerId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<TicketResponse>> getTicketsByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(ticketService.getTicketsByPassenger(passengerId));
    }

    @GetMapping("/passenger/{passengerId}/total-spent")
    @IsEmployeeOrAdmin
    public ResponseEntity<BigDecimal> getTotalSpentByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(ticketService.getTotalSpentByPassenger(passengerId));
    }

    @GetMapping("/user/{userId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<TicketResponse>> getTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @PatchMapping("/{id}/status")
    @IsEmployeeOrAdmin
    public ResponseEntity<TicketResponse> updateTicketStatus(
        @PathVariable Long id,
        @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, request));
    }

    @DeleteMapping("/{id}/cancel")
    @IsEmployeeOrAdmin
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/flight/{flightId}/available-seats")
    public ResponseEntity<AvailableSeatsResponse> getAvailableSeats(@PathVariable Long flightId) {
        return ResponseEntity.ok(ticketService.getAvailableSeats(flightId));
    }
}
