package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.*;
import ru.java.airport.exception.*;
import ru.java.airport.repository.AircraftRepository;
import ru.java.airport.repository.AirlineRepository;
import ru.java.airport.repository.AirportRepository;
import ru.java.airport.repository.FlightRepository;
import ru.java.airport.web.dto.flight.request.CreateFlightRequest;
import ru.java.airport.web.dto.flight.request.UpdateFlightRequest;
import ru.java.airport.web.dto.flight.request.UpdateFlightStatusRequest;
import ru.java.airport.web.dto.flight.response.CrewBriefResponse;
import ru.java.airport.web.dto.flight.response.FlightResponse;
import ru.java.airport.web.dto.flight.response.GetFlightResponse;
import ru.java.airport.web.dto.flight.response.TicketBriefResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;

    @Transactional
    public FlightResponse createFlight(CreateFlightRequest request) {
        if (flightRepository.findByFlightNumber(request.flightNumber()).isPresent()) {
            throw new FlightAlreadyExistsException("Flight number already exists: " + request.flightNumber());
        }

        if (request.scheduledDeparture().isAfter(request.scheduledArrival())) {
            throw new RuntimeException("Scheduled departure must be before scheduled arrival");
        }

        Airline airline = airlineRepository.findById(request.airlineId())
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        Aircraft aircraft = aircraftRepository.findById(request.aircraftId())
            .orElseThrow(() -> new AircraftNotFoundException(Constants.AIRCRAFT_NOT_FOUND_MESSAGE));

        Airport departureAirport = airportRepository.findById(request.departureAirportId())
            .orElseThrow(() -> new AirportNotFoundException("Departure airport not found with id: " + request.departureAirportId()));

        Airport arrivalAirport = airportRepository.findById(request.arrivalAirportId())
            .orElseThrow(() -> new AirportNotFoundException("Arrival airport not found with id: " + request.arrivalAirportId()));

        Flight flight = new Flight();
        flight.setFlightNumber(request.flightNumber());
        flight.setScheduledDeparture(request.scheduledDeparture());
        flight.setScheduledArrival(request.scheduledArrival());
        flight.setAirline(airline);
        flight.setAircraft(aircraft);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setStatus(FlightStatus.SCHEDULED);

        Flight savedFlight = flightRepository.save(flight);
        return toFlightResponse(savedFlight);
    }

    @Transactional
    public FlightResponse updateFlight(Long id, UpdateFlightRequest request) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        if (request.scheduledDeparture() != null && request.scheduledArrival() != null) {
            if (request.scheduledDeparture().isAfter(request.scheduledArrival())) {
                throw new RuntimeException("Scheduled departure must be before scheduled arrival");
            }
            flight.setScheduledDeparture(request.scheduledDeparture());
            flight.setScheduledArrival(request.scheduledArrival());
        } else if (request.scheduledDeparture() != null) {
            flight.setScheduledDeparture(request.scheduledDeparture());
        } else if (request.scheduledArrival() != null) {
            flight.setScheduledArrival(request.scheduledArrival());
        }

        if (request.status() != null) {
            flight.setStatus(request.status());
        }

        Flight updatedFlight = flightRepository.save(flight);
        return toFlightResponse(updatedFlight);
    }

    @Transactional
    public FlightResponse updateFlightStatus(Long id, UpdateFlightStatusRequest request) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        FlightStatus newStatus = request.status();

        if (newStatus == FlightStatus.DEPARTED && flight.getActualDeparture() == null) {
            flight.setActualDeparture(LocalDateTime.now());
        }

        if (newStatus == FlightStatus.ARRIVED && flight.getActualArrival() == null) {
            flight.setActualArrival(LocalDateTime.now());
        }

        flight.setStatus(newStatus);
        Flight updatedFlight = flightRepository.save(flight);
        return toFlightResponse(updatedFlight);
    }

    public FlightResponse getFlight(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));
        return toFlightResponse(flight);
    }

    public GetFlightResponse getFlightWithDetails(Long id) {
        Flight flight = flightRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        List<TicketBriefResponse> tickets = flight.getTickets()
            .stream()
            .map(ticket -> new TicketBriefResponse(
                ticket.getId(),
                ticket.getSeatNumber(),
                ticket.getPrice().toString(),
                ticket.getPassenger().getFirstName() + " " + ticket.getPassenger().getLastName(),
                ticket.getStatus()
            ))
            .collect(Collectors.toList());

        List<CrewBriefResponse> crew = flight.getCrewMembers()
            .stream()
            .map(crewMember -> new CrewBriefResponse(
                crewMember.getId(),
                crewMember.getEmployee().getId(),
                crewMember.getEmployee().getFirstName() + " " + crewMember.getEmployee().getLastName(),
                crewMember.getRoleOnFlight()
            ))
            .collect(Collectors.toList());

        return new GetFlightResponse(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getAirline().getId(),
            flight.getAirline().getName(),
            flight.getAircraft().getId(),
            flight.getAircraft().getModel(),
            flight.getDepartureAirport().getId(),
            flight.getDepartureAirport().getName(),
            flight.getDepartureAirport().getCity(),
            flight.getArrivalAirport().getId(),
            flight.getArrivalAirport().getName(),
            flight.getArrivalAirport().getCity(),
            flight.getScheduledDeparture(),
            flight.getScheduledArrival(),
            flight.getActualDeparture(),
            flight.getActualArrival(),
            flight.getStatus(),
            tickets,
            crew
        );
    }

    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll()
            .stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());
    }

    public List<FlightResponse> getFlightsByStatus(FlightStatus status) {
        return flightRepository.findByStatus(status)
            .stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());
    }

    public List<FlightResponse> getFlightsByAirline(Long airlineId) {
        airlineRepository.findById(airlineId)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        return flightRepository.findByAirlineId(airlineId)
            .stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());
    }

    public List<FlightResponse> getFlightsByRoute(Long departureAirportId, Long arrivalAirportId) {
        airportRepository.findById(departureAirportId)
            .orElseThrow(() -> new AirportNotFoundException("Departure airport not found with id: " + departureAirportId));
        airportRepository.findById(arrivalAirportId)
            .orElseThrow(() -> new AirportNotFoundException("Arrival airport not found with id: " + arrivalAirportId));

        return flightRepository.findByDepartureAirportIdAndArrivalAirportId(departureAirportId, arrivalAirportId)
            .stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());
    }

    public List<FlightResponse> getFlightsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start date must be before end date");
        }

        return flightRepository.findByScheduledDepartureBetween(start, end)
            .stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        flightRepository.delete(flight);
    }

    private FlightResponse toFlightResponse(Flight flight) {
        return new FlightResponse(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getAirline().getName(),
            flight.getAircraft().getModel(),
            flight.getDepartureAirport().getName(),
            flight.getArrivalAirport().getName(),
            flight.getScheduledDeparture(),
            flight.getScheduledArrival(),
            flight.getStatus(),
            flight.getPrice()
        );
    }
}
