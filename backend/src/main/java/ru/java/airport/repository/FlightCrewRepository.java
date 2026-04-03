package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.FlightCrew;

import java.util.List;

@Repository
public interface FlightCrewRepository extends JpaRepository<FlightCrew, Long> {
    List<FlightCrew> findByFlightId(Long flightId);

    List<FlightCrew> findByEmployeeId(Long employeeId);

    List<FlightCrew> findByFlightIdAndRoleOnFlight(Long flightId, String roleOnFlight);

    @Query("""
            SELECT fc FROM FlightCrew fc
            LEFT JOIN FETCH fc.employee
            WHERE fc.flight.id = :flightId
            """)
    List<FlightCrew> findByFlightIdWithEmployees(Long flightId);

    @Query("""
            SELECT fc FROM FlightCrew fc
            LEFT JOIN FETCH fc.flight f
            LEFT JOIN FETCH f.departureAirport
            LEFT JOIN FETCH f.arrivalAirport
            WHERE fc.employee.id = :employeeId
            """)
    List<FlightCrew> findByEmployeeIdWithFlights(Long employeeId);

    boolean existsByFlightIdAndEmployeeId(Long flightId, Long employeeId);
}
