package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Passenger;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByPassportNumber(String passportNumber);

    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhone(String phone);

    Optional<Passenger> findByUserId(Long userId);

    List<Passenger> findByLastName(String lastName);

    @Query("SELECT p FROM Passenger p LEFT JOIN FETCH p.tickets WHERE p.id = :id")
    Optional<Passenger> findByIdWithTickets(Long id);

    @Query("SELECT p FROM Passenger p LEFT JOIN FETCH p.tickets WHERE p.user.id = :userId")
    Optional<Passenger> findByUserIdWithTickets(Long userId);

    @Query("""
            SELECT p FROM Passenger p
            LEFT JOIN p.tickets t
            WHERE t.flight.id = :flightId
            """)
    List<Passenger> findByFlightId(Long flightId);
}
