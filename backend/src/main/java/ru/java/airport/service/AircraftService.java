package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Aircraft;
import ru.java.airport.entity.Airline;
import ru.java.airport.exception.AircraftAlreadyExistsException;
import ru.java.airport.exception.AircraftNotFoundException;
import ru.java.airport.exception.AirlineNotFoundException;
import ru.java.airport.repository.AircraftRepository;
import ru.java.airport.repository.AirlineRepository;
import ru.java.airport.web.dto.aircraft.request.CreateAircraftRequest;
import ru.java.airport.web.dto.aircraft.request.UpdateAircraftRequest;
import ru.java.airport.web.dto.aircraft.response.AircraftResponse;
import ru.java.airport.web.dto.aircraft.response.GetAircraftResponse;
import ru.java.airport.web.dto.airport.response.FlightBriefResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AircraftService {
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Transactional
    public AircraftResponse createAircraft(CreateAircraftRequest request) {
        if (aircraftRepository.findByRegistrationNumber(request.registrationNumber()).isPresent()) {
            throw new AircraftAlreadyExistsException("Registration number already exists: " + request.registrationNumber());
        }

        Airline airline = airlineRepository.findById(request.airlineId())
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        Aircraft aircraft = new Aircraft();
        aircraft.setModel(request.model());
        aircraft.setCapacity(request.capacity());
        aircraft.setRegistrationNumber(request.registrationNumber());
        aircraft.setAirline(airline);

        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return toAircraftResponse(savedAircraft);
    }

    @Transactional
    public AircraftResponse updateAircraft(Long id, UpdateAircraftRequest request) {
        Aircraft aircraft = aircraftRepository.findById(id)
            .orElseThrow(() -> new AircraftNotFoundException(Constants.AIRCRAFT_NOT_FOUND_MESSAGE));

        if (request.model() != null) {
            aircraft.setModel(request.model());
        }
        if (request.capacity() != null) {
            aircraft.setCapacity(request.capacity());
        }
        if (request.registrationNumber() != null) {
            if (!request.registrationNumber().equals(aircraft.getRegistrationNumber()) &&
                aircraftRepository.findByRegistrationNumber(request.registrationNumber()).isPresent()) {
                throw new AircraftAlreadyExistsException("Registration number already exists: " + request.registrationNumber());
            }
            aircraft.setRegistrationNumber(request.registrationNumber());
        }

        Aircraft updatedAircraft = aircraftRepository.save(aircraft);
        return toAircraftResponse(updatedAircraft);
    }

    public AircraftResponse getAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
            .orElseThrow(() -> new AircraftNotFoundException(Constants.AIRCRAFT_NOT_FOUND_MESSAGE));
        return toAircraftResponse(aircraft);
    }

    public GetAircraftResponse getAircraftWithFlights(Long id) {
        Aircraft aircraft = aircraftRepository.findByIdWithFlights(id)
            .orElseThrow(() -> new AircraftNotFoundException(Constants.AIRCRAFT_NOT_FOUND_MESSAGE));

        List<FlightBriefResponse> flights = aircraft.getFlights().stream()
            .map(flight -> new FlightBriefResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline().getName(),
                flight.getScheduledDeparture().toString(),
                flight.getStatus()
            ))
            .collect(Collectors.toList());

        return new GetAircraftResponse(
            aircraft.getId(),
            aircraft.getModel(),
            aircraft.getCapacity(),
            aircraft.getRegistrationNumber(),
            aircraft.getAirline().getId(),
            aircraft.getAirline().getName(),
            flights
        );
    }

    public List<AircraftResponse> getAllAircrafts() {
        return aircraftRepository.findAll()
            .stream()
            .map(this::toAircraftResponse)
            .collect(Collectors.toList());
    }

    public List<AircraftResponse> getAircraftsByAirline(Long airlineId) {
        airlineRepository.findById(airlineId)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        return aircraftRepository.findByAirlineId(airlineId).stream()
            .map(this::toAircraftResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
            .orElseThrow(() -> new AircraftNotFoundException(Constants.AIRCRAFT_NOT_FOUND_MESSAGE));
        aircraftRepository.delete(aircraft);
    }

    private AircraftResponse toAircraftResponse(Aircraft aircraft) {
        return new AircraftResponse(
            aircraft.getId(),
            aircraft.getModel(),
            aircraft.getCapacity(),
            aircraft.getRegistrationNumber(),
            aircraft.getAirline().getId(),
            aircraft.getAirline().getName()
        );
    }
}
