package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Passenger;
import ru.java.airport.entity.User;
import ru.java.airport.exception.PassengerAlreadyExistsException;
import ru.java.airport.exception.PassengerNotFoundException;
import ru.java.airport.exception.UserNotFoundException;
import ru.java.airport.repository.PassengerRepository;
import ru.java.airport.repository.UserRepository;
import ru.java.airport.web.dto.passenger.request.CreatePassengerRequest;
import ru.java.airport.web.dto.passenger.request.UpdatePassengerRequest;
import ru.java.airport.web.dto.passenger.response.GetPassengerResponse;
import ru.java.airport.web.dto.passenger.response.PassengerResponse;
import ru.java.airport.web.dto.passenger.response.TicketInfoResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;

    @Transactional
    public PassengerResponse createPassenger(CreatePassengerRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        if (user.getPassenger() != null) {
            throw new PassengerAlreadyExistsException("User already has a passenger profile");
        }

        if (passengerRepository.findByPassportNumber(request.passportNumber()).isPresent()) {
            throw new PassengerAlreadyExistsException("User with this passport number already exists");
        }

        if (passengerRepository.findByPhone(request.phone()).isPresent()) {
            throw new PassengerAlreadyExistsException("User with this phone number already exists");
        }

        if (passengerRepository.findByEmail(request.email()).isPresent()) {
            throw new PassengerAlreadyExistsException("User with this email already exists");
        }

        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setPassportNumber(request.passportNumber());
        passenger.setPhone(request.phone());
        passenger.setEmail(request.email());

        Passenger savedPassenger = passengerRepository.save(passenger);
        return toPassengerResponse(savedPassenger);
    }

    @Transactional
    public PassengerResponse updatePassenger(Long id, UpdatePassengerRequest request) {
        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        if (passengerRepository.findByPhone(request.phone()).isPresent()) {
            throw new PassengerAlreadyExistsException("This phone number is already in use");
        }

        if (passengerRepository.findByEmail(request.email()).isPresent()) {
            throw new PassengerAlreadyExistsException("This email is already in use");
        }

        if (request.firstName() != null) {
            passenger.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            passenger.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            passenger.setPhone(request.phone());
        }
        if (request.email() != null) {
            passenger.setEmail(request.email());
        }

        Passenger updatedPassenger = passengerRepository.save(passenger);
        return toPassengerResponse(updatedPassenger);
    }

    public PassengerResponse getPassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));
        return toPassengerResponse(passenger);
    }

    public GetPassengerResponse getPassengerWithTickets(Long id) {
        Passenger passenger = passengerRepository.findByIdWithTickets(id)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));

        List<TicketInfoResponse> tickets = passenger.getTickets().stream()
            .map(ticket -> new TicketInfoResponse(
                ticket.getId(),
                ticket.getFlight().getFlightNumber(),
                ticket.getSeatNumber(),
                ticket.getPrice().toString(),
                ticket.getStatus(),
                ticket.getPurchaseDate()
            ))
            .collect(Collectors.toList());

        return new GetPassengerResponse(
            passenger.getId(),
            passenger.getFirstName(),
            passenger.getLastName(),
            passenger.getPassportNumber(),
            passenger.getPhone(),
            passenger.getEmail(),
            passenger.getUser().getId(),
            tickets
        );
    }

    public PassengerResponse getPassengerByUserId(Long userId) {
        Passenger passenger = passengerRepository.findByUserId(userId)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));
        return toPassengerResponse(passenger);
    }

    public List<PassengerResponse> getPassengersByLastName(String lastName) {
        return passengerRepository.findByLastName(lastName).stream()
            .map(this::toPassengerResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(Constants.PASSENGER_NOT_FOUND_MESSAGE));
        passengerRepository.delete(passenger);
    }

    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findAll()
            .stream()
            .map(this::toPassengerResponse)
            .collect(Collectors.toList());
    }

    private PassengerResponse toPassengerResponse(Passenger passenger) {
        return new PassengerResponse(
            passenger.getId(),
            passenger.getFirstName(),
            passenger.getLastName(),
            passenger.getPassportNumber(),
            passenger.getPhone(),
            passenger.getEmail(),
            passenger.getUser().getId()
        );
    }
}
