package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Airline;
import ru.java.airport.exception.AirlineAlreadyExistsException;
import ru.java.airport.exception.AirlineNotFoundException;
import ru.java.airport.repository.AirlineRepository;
import ru.java.airport.web.dto.airline.request.CreateAirlineRequest;
import ru.java.airport.web.dto.airline.request.UpdateAirlineRequest;
import ru.java.airport.web.dto.airline.response.AircraftInfoResponse;
import ru.java.airport.web.dto.airline.response.AirlineResponse;
import ru.java.airport.web.dto.airline.response.FlightInfoResponse;
import ru.java.airport.web.dto.airline.response.GetAirlineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirlineService {

    private final AirlineRepository airlineRepository;

    @Transactional
    public AirlineResponse createAirline(CreateAirlineRequest request) {
        if (request.iataCode() != null && airlineRepository.findByIataCode(request.iataCode()).isPresent()) {
            throw new AirlineAlreadyExistsException("IATA code already exists: " + request.iataCode());
        }

        Airline airline = new Airline();
        airline.setName(request.name());
        airline.setCountry(request.country());
        airline.setIataCode(request.iataCode());

        Airline savedAirline = airlineRepository.save(airline);
        return toAirlineResponse(savedAirline);
    }

    @Transactional
    public AirlineResponse updateAirline(Long id, UpdateAirlineRequest request) {
        Airline airline = airlineRepository.findById(id)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        if (request.name() != null) {
            airline.setName(request.name());
        }
        if (request.country() != null) {
            airline.setCountry(request.country());
        }
        if (request.iataCode() != null) {
            if (!request.iataCode().equals(airline.getIataCode()) &&
                airlineRepository.findByIataCode(request.iataCode()).isPresent()) {
                throw new AirlineAlreadyExistsException("IATA code already exists: " + request.iataCode());
            }
            airline.setIataCode(request.iataCode());
        }

        Airline updatedAirline = airlineRepository.save(airline);
        return toAirlineResponse(updatedAirline);
    }

    public AirlineResponse getAirline(Long id) {
        Airline airline = airlineRepository.findById(id)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));
        return toAirlineResponse(airline);
    }

    public GetAirlineResponse getAirlineWithDetails(Long id) {
        Airline airline = airlineRepository.findByIdWithAircrafts(id)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));

        List<AircraftInfoResponse> aircrafts = airline.getAircrafts().stream()
            .map(aircraft -> new AircraftInfoResponse(
                aircraft.getId(),
                aircraft.getModel(),
                aircraft.getCapacity(),
                aircraft.getRegistrationNumber()
            ))
            .collect(Collectors.toList());

        List<FlightInfoResponse> flights = airline.getFlights().stream()
            .map(flight -> new FlightInfoResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureAirport().getCity(),
                flight.getArrivalAirport().getCity(),
                flight.getStatus()
            ))
            .collect(Collectors.toList());

        return new GetAirlineResponse(
            airline.getId(),
            airline.getName(),
            airline.getCountry(),
            airline.getIataCode(),
            aircrafts,
            flights
        );
    }

    public List<AirlineResponse> getAllAirlines() {
        return airlineRepository.findAll().stream()
            .map(this::toAirlineResponse)
            .collect(Collectors.toList());
    }

    public List<AirlineResponse> getAirlinesByCountry(String country) {
        return airlineRepository.findByCountry(country)
            .stream()
            .map(this::toAirlineResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAirline(Long id) {
        Airline airline = airlineRepository.findById(id)
            .orElseThrow(() -> new AirlineNotFoundException(Constants.AIRLINE_NOT_FOUND_MESSAGE));
        airlineRepository.delete(airline);
    }

    private AirlineResponse toAirlineResponse(Airline airline) {
        return new AirlineResponse(
            airline.getId(),
            airline.getName(),
            airline.getCountry(),
            airline.getIataCode()
        );
    }
}
