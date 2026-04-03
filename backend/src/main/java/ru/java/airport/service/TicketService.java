package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.*;
import ru.java.airport.exception.*;
import ru.java.airport.repository.FlightRepository;
import ru.java.airport.repository.PassengerRepository;
import ru.java.airport.repository.TicketRepository;
import ru.java.airport.repository.UserRepository;
import ru.java.airport.utils.Utils;
import ru.java.airport.web.dto.ticket.request.UpdateTicketStatusRequest;
import ru.java.airport.web.dto.ticket.response.AvailableSeatsResponse;
import ru.java.airport.web.dto.ticket.response.GetTicketResponse;
import ru.java.airport.web.dto.ticket.response.TicketResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ru.java.airport.utils.Utils.generateAllSeats;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {
    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketResponse createTicket(Long flightId, Long passengerId, String seatNumber, BigDecimal price) {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new BusinessException("Cannot buy ticket for cancelled flight");
        }

        Aircraft aircraft = flight.getAircraft();

        if (!Utils.isValidSeat(aircraft, seatNumber)) {
            throw new BusinessException("Invalid seat number: %s".formatted(seatNumber));
        }

        if (ticketRepository.existsByFlightIdAndSeatNumber(flightId, seatNumber)) {
            throw new TicketAlreadyExistsException("Seat %s is already occupied".formatted(seatNumber));
        }

        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        Ticket ticket = new Ticket();
        ticket.setFlight(flight);
        ticket.setPassenger(passenger);
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(price);
        ticket.setStatus(TicketStatus.CONFIRMED);

        Ticket savedTicket = ticketRepository.save(ticket);
        return toTicketResponse(savedTicket);
    }

    @Transactional
    public TicketResponse updateTicketStatus(Long id, UpdateTicketStatusRequest request) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));

        Flight flight = ticket.getFlight();

        if (request.status() == TicketStatus.CANCELLED) {
            if (flight.getStatus() == FlightStatus.DEPARTED ||
                flight.getStatus() == FlightStatus.IN_AIR ||
                flight.getStatus() == FlightStatus.ARRIVED) {
                throw new BusinessException(Constants.FLIGHT_ALREADY_STARTED_MESSAGE);
            }
        }

        if (request.status() == TicketStatus.CHECKED_IN || request.status() == TicketStatus.BOARDED) {
            if (flight.getStatus() != FlightStatus.BOARDING && flight.getStatus() != FlightStatus.DEPARTED) {
                throw new BusinessException("Cannot check-in or board before boarding time");
            }
        }

        ticket.setStatus(request.status());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return toTicketResponse(updatedTicket);
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));
        return toTicketResponse(ticket);
    }

    public GetTicketResponse getTicketWithDetails(Long id) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));

        Flight flight = ticket.getFlight();
        Passenger passenger = ticket.getPassenger();

        return new GetTicketResponse(
            ticket.getId(),
            flight.getId(),
            flight.getFlightNumber(),
            flight.getDepartureAirport().getName(),
            flight.getArrivalAirport().getName(),
            flight.getScheduledDeparture(),
            passenger.getId(),
            passenger.getFirstName() + " " + passenger.getLastName(),
            passenger.getPassportNumber(),
            ticket.getSeatNumber(),
            ticket.getPrice(),
            ticket.getPurchaseDate(),
            ticket.getStatus()
        );
    }

    @Transactional
    public GetTicketResponse getMyTicketWithDetails(Long id, Long currentUserId) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));

        if (!ticket.getPassenger().getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException();
        }

        Flight flight = ticket.getFlight();
        Passenger passenger = ticket.getPassenger();

        return new GetTicketResponse(
            ticket.getId(),
            flight.getId(),
            flight.getFlightNumber(),
            flight.getDepartureAirport().getName(),
            flight.getArrivalAirport().getName(),
            flight.getScheduledDeparture(),
            passenger.getId(),
            passenger.getFirstName() + " " + passenger.getLastName(),
            passenger.getPassportNumber(),
            ticket.getSeatNumber(),
            ticket.getPrice(),
            ticket.getPurchaseDate(),
            ticket.getStatus()
        );
    }

    public List<TicketResponse> getMyTicketsByStatus(Long userId, TicketStatus status) {
        Passenger passenger = passengerRepository.findByUserId(userId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        return ticketRepository.findByPassengerIdAndStatus(passenger.getId(), status).stream()
            .map(this::toTicketResponse)
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByPassenger(Long passengerId) {
        passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        return ticketRepository.findByPassengerId(passengerId)
            .stream()
            .map(this::toTicketResponse)
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByFlight(Long flightId) {
        flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        return ticketRepository.findByFlightId(flightId)
            .stream()
            .map(this::toTicketResponse)
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status)
            .stream()
            .map(this::toTicketResponse)
            .collect(Collectors.toList());
    }

    public BigDecimal getTotalSpentByPassenger(Long passengerId) {
        passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        BigDecimal total = ticketRepository.getTotalSpentByPassenger(passengerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalSpentByUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        BigDecimal total = ticketRepository.getTotalSpentByPassenger(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getOccupiedSeatsCount(Long flightId) {
        flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        return ticketRepository.countConfirmedTicketsByFlight(flightId);
    }

    @Transactional
    public void cancelTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));

        Flight flight = ticket.getFlight();

        if (flight.getStatus() == FlightStatus.DEPARTED ||
            flight.getStatus() == FlightStatus.IN_AIR ||
            flight.getStatus() == FlightStatus.ARRIVED) {
            throw new BusinessException(Constants.FLIGHT_ALREADY_STARTED_MESSAGE);
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicketByUser(Long ticketId, Long currentUserId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(Constants.TICKET_NOT_FOUND_MESSAGE));

        if (!ticket.getPassenger().getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException();
        }

        Flight flight = ticket.getFlight();

        if (flight.getStatus() == FlightStatus.DEPARTED ||
            flight.getStatus() == FlightStatus.IN_AIR ||
            flight.getStatus() == FlightStatus.ARRIVED) {
            throw new BusinessException(Constants.FLIGHT_ALREADY_STARTED_MESSAGE);
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
            .stream()
            .map(this::toTicketResponse)
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByUserId(Long userId) {
        Passenger passenger = passengerRepository.findByUserId(userId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));
        return getTicketsByPassenger(passenger.getId());
    }

    public AvailableSeatsResponse getAvailableSeats(Long flightId) {
        // Получаем рейс
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        // Получаем самолет и его вместимость
        Aircraft aircraft = flight.getAircraft();
        Integer totalSeats = aircraft.getCapacity();

        // Получаем занятые места
        List<String> occupiedSeats = ticketRepository.findOccupiedSeatsByFlightId(flightId);

        // Генерируем все возможные места
        List<String> allSeats = generateAllSeats(aircraft);

        // Вычисляем свободные места
        List<String> availableSeats = allSeats.stream()
            .filter(seat -> !occupiedSeats.contains(seat))
            .collect(Collectors.toList());

        return new AvailableSeatsResponse(
            flight.getId(),
            flight.getFlightNumber(),
            totalSeats,
            occupiedSeats.size(),
            availableSeats.size(),
            availableSeats,
            occupiedSeats
        );
    }

    private TicketResponse toTicketResponse(Ticket ticket) {
        return new TicketResponse(
            ticket.getId(),
            ticket.getFlight().getId(),
            ticket.getFlight().getFlightNumber(),
            ticket.getPassenger().getId(),
            ticket.getPassenger().getFirstName() + " " + ticket.getPassenger().getLastName(),
            ticket.getSeatNumber(),
            ticket.getPrice(),
            ticket.getPurchaseDate(),
            ticket.getStatus()
        );
    }
}
