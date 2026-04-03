package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.entity.Ticket;
import ru.java.airport.entity.TicketStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPassengerId(Long passengerId);

    List<Ticket> findByFlightId(Long flightId);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPassengerIdAndStatus(Long passengerId, TicketStatus status);

    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);

    @Query("""
            SELECT t FROM Ticket t
            LEFT JOIN FETCH t.passenger
            LEFT JOIN FETCH t.flight
            WHERE t.id = :id
            """)
    Optional<Ticket> findByIdWithDetails(Long id);

    @Query("""
            SELECT t FROM Ticket t
            LEFT JOIN FETCH t.flight f
            LEFT JOIN FETCH f.departureAirport
            LEFT JOIN FETCH f.arrivalAirport
            WHERE t.passenger.id = :passengerId
            """)
    List<Ticket> findByPassengerIdWithFlightDetails(Long passengerId);

    @Query("SELECT SUM(t.price) FROM Ticket t WHERE t.passenger.id = :passengerId AND t.status = 'CONFIRMED'")
    BigDecimal getTotalSpentByPassenger(Long passengerId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.flight.id = :flightId AND t.status = 'CONFIRMED'")
    Long countConfirmedTicketsByFlight(Long flightId);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = 'CANCELLED' WHERE t.id = :id")
    void cancelTicket(Long id);

    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.flight.id = :flightId AND t.status != 'CANCELLED'")
    List<String> findOccupiedSeatsByFlightId(Long flightId);
}
