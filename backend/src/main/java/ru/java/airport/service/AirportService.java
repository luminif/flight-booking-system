package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Airport;
import ru.java.airport.exception.AirportAlreadyExistsException;
import ru.java.airport.exception.AirportNotFoundException;
import ru.java.airport.repository.AirportRepository;
import ru.java.airport.web.dto.airport.request.CreateAirportRequest;
import ru.java.airport.web.dto.airport.request.UpdateAirportRequest;
import ru.java.airport.web.dto.airport.response.AirportResponse;
import ru.java.airport.web.dto.airport.response.FlightBriefResponse;
import ru.java.airport.web.dto.airport.response.GetAirportResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportService {
    private final AirportRepository airportRepository;

    @Transactional
    public AirportResponse createAirport(CreateAirportRequest request) {
        if (request.iataCode() != null && airportRepository.findByIataCode(request.iataCode()).isPresent()) {
            throw new AirportAlreadyExistsException("IATA code already exists: " + request.iataCode());
        }

        Airport airport = new Airport();
        airport.setName(request.name());
        airport.setCity(request.city());
        airport.setCountry(request.country());
        airport.setIataCode(request.iataCode());

        Airport savedAirport = airportRepository.save(airport);
        return toAirportResponse(savedAirport);
    }

    @Transactional
    public AirportResponse updateAirport(Long id, UpdateAirportRequest request) {
        Airport airport = airportRepository.findById(id)
            .orElseThrow(() -> new AirportNotFoundException(Constants.AIRPORT_NOT_FOUND_MESSAGE));

        if (request.name() != null) {
            airport.setName(request.name());
        }
        if (request.city() != null) {
            airport.setCity(request.city());
        }
        if (request.country() != null) {
            airport.setCountry(request.country());
        }
        if (request.iataCode() != null) {
            if (!request.iataCode().equals(airport.getIataCode()) &&
                airportRepository.findByIataCode(request.iataCode()).isPresent()) {
                throw new AirportAlreadyExistsException("IATA code already exists: " + request.iataCode());
            }
            airport.setIataCode(request.iataCode());
        }

        Airport updatedAirport = airportRepository.save(airport);
        return toAirportResponse(updatedAirport);
    }

    public AirportResponse getAirport(Long id) {
        Airport airport = airportRepository.findById(id)
            .orElseThrow(() -> new AirportNotFoundException(Constants.AIRPORT_NOT_FOUND_MESSAGE));
        return toAirportResponse(airport);
    }

    public GetAirportResponse getAirportWithFlights(Long id) {
        Airport airport = airportRepository.findById(id)
            .orElseThrow(() -> new AirportNotFoundException(Constants.AIRPORT_NOT_FOUND_MESSAGE));

        List<FlightBriefResponse> departingFlights = airport.getDepartingFlights().stream()
            .map(flight -> new FlightBriefResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline().getName(),
                flight.getScheduledDeparture().toString(),
                flight.getStatus()
            ))
            .collect(Collectors.toList());

        List<FlightBriefResponse> arrivingFlights = airport.getArrivingFlights().stream()
            .map(flight -> new FlightBriefResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline().getName(),
                flight.getScheduledDeparture().toString(),
                flight.getStatus()
            ))
            .collect(Collectors.toList());

        return new GetAirportResponse(
            airport.getId(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry(),
            airport.getIataCode(),
            departingFlights,
            arrivingFlights
        );
    }

    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
            .map(this::toAirportResponse)
            .collect(Collectors.toList());
    }

    public List<AirportResponse> getAirportsByCity(String city) {
        return airportRepository.findByCity(city).stream()
            .map(this::toAirportResponse)
            .collect(Collectors.toList());
    }

    public List<AirportResponse> getAirportsByCountry(String country) {
        return airportRepository.findByCountry(country).stream()
            .map(this::toAirportResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAirport(Long id) {
        Airport airport = airportRepository.findById(id)
            .orElseThrow(() -> new AirportNotFoundException(Constants.AIRPORT_NOT_FOUND_MESSAGE));
        airportRepository.delete(airport);
    }

    private AirportResponse toAirportResponse(Airport airport) {
        return new AirportResponse(
            airport.getId(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry(),
            airport.getIataCode()
        );
    }
}
