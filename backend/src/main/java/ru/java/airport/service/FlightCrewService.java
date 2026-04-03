package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Employee;
import ru.java.airport.entity.Flight;
import ru.java.airport.entity.FlightCrew;
import ru.java.airport.exception.BusinessException;
import ru.java.airport.exception.EmployeeNotFoundException;
import ru.java.airport.exception.FlightCrewNotFoundException;
import ru.java.airport.exception.FlightNotFoundException;
import ru.java.airport.repository.EmployeeRepository;
import ru.java.airport.repository.FlightCrewRepository;
import ru.java.airport.repository.FlightRepository;
import ru.java.airport.web.dto.flightcrew.request.AssignCrewRequest;
import ru.java.airport.web.dto.flightcrew.response.FlightCrewResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightCrewService {
    private final FlightCrewRepository flightCrewRepository;
    private final FlightRepository flightRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public FlightCrewResponse assignCrew(AssignCrewRequest request) {
        Flight flight = flightRepository.findById(request.flightId())
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        Employee employee = employeeRepository.findById(request.employeeId())
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));

        if (flightCrewRepository.existsByFlightIdAndEmployeeId(request.flightId(), request.employeeId())) {
            throw new BusinessException("Employee already assigned to this flight");
        }

        FlightCrew flightCrew = new FlightCrew();
        flightCrew.setFlight(flight);
        flightCrew.setEmployee(employee);
        flightCrew.setRoleOnFlight(request.roleOnFlight());

        FlightCrew savedFlightCrew = flightCrewRepository.save(flightCrew);
        return toFlightCrewResponse(savedFlightCrew);
    }

    @Transactional
    public void removeCrew(Long id) {
        FlightCrew flightCrew = flightCrewRepository.findById(id)
            .orElseThrow(() -> new FlightCrewNotFoundException(Constants.FLIGHT_CREW_NOT_FOUND_MESSAGE));
        flightCrewRepository.delete(flightCrew);
    }

    @Transactional
    public FlightCrewResponse updateRole(Long id, String role) {
        FlightCrew flightCrew = flightCrewRepository.findById(id)
            .orElseThrow(() -> new FlightCrewNotFoundException(Constants.FLIGHT_CREW_NOT_FOUND_MESSAGE));

        flightCrew.setRoleOnFlight(role);
        FlightCrew updated = flightCrewRepository.save(flightCrew);
        return toFlightCrewResponse(updated);
    }

    public List<FlightCrewResponse> getCrewByFlight(Long flightId) {
        flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        return flightCrewRepository.findByFlightId(flightId)
            .stream()
            .map(this::toFlightCrewResponse)
            .collect(Collectors.toList());
    }

    public List<FlightCrewResponse> getCrewByEmployee(Long employeeId) {
        employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));

        return flightCrewRepository.findByEmployeeId(employeeId)
            .stream()
            .map(this::toFlightCrewResponse)
            .collect(Collectors.toList());
    }

    public List<FlightCrewResponse> getCrewByFlightAndRole(Long flightId, String role) {
        flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        return flightCrewRepository.findByFlightIdAndRoleOnFlight(flightId, role)
            .stream()
            .map(this::toFlightCrewResponse)
            .collect(Collectors.toList());
    }

    public List<FlightCrewResponse> getCrewByFlightWithDetails(Long flightId) {
        flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(Constants.FLIGHT_NOT_FOUND_MESSAGE));

        return flightCrewRepository.findByFlightIdWithEmployees(flightId)
            .stream()
            .map(this::toFlightCrewResponse)
            .collect(Collectors.toList());
    }

    private FlightCrewResponse toFlightCrewResponse(FlightCrew flightCrew) {
        return new FlightCrewResponse(
            flightCrew.getId(),
            flightCrew.getFlight().getId(),
            flightCrew.getFlight().getFlightNumber(),
            flightCrew.getEmployee().getId(),
            flightCrew.getEmployee().getFirstName() + " " + flightCrew.getEmployee().getLastName(),
            flightCrew.getRoleOnFlight()
        );
    }
}
