package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Flight;
import ru.java.airport.entity.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByStatus(FlightStatus status);

    List<Flight> findByAirlineId(Long airlineId);

    List<Flight> findByDepartureAirportIdAndArrivalAirportId(Long departureId, Long arrivalId);

    List<Flight> findByScheduledDepartureBetween(LocalDateTime start, LocalDateTime end);

    List<Flight> findByDepartureAirportIdAndScheduledDepartureBetween(
        Long airportId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT f FROM Flight f WHERE f.status = 'DELAYED'")
    List<Flight> findDelayedFlights();

    @Query("""
            SELECT f FROM Flight f
            LEFT JOIN FETCH f.airline
            LEFT JOIN FETCH f.aircraft
            LEFT JOIN FETCH f.departureAirport
            LEFT JOIN FETCH f.arrivalAirport
            WHERE f.id = :id
            """)
    Optional<Flight> findByIdWithDetails(Long id);

    @Query("SELECT f FROM Flight f LEFT JOIN FETCH f.tickets WHERE f.id = :id")
    Optional<Flight> findByIdWithTickets(Long id);

    @Query("SELECT f FROM Flight f LEFT JOIN FETCH f.crewMembers WHERE f.id = :id")
    Optional<Flight> findByIdWithCrew(Long id);

    @Query("""
            SELECT f FROM Flight f
            LEFT JOIN FETCH f.airline
            LEFT JOIN FETCH f.departureAirport
            LEFT JOIN FETCH f.arrivalAirport
            """)
    List<Flight> findAllWithDetails();
}
