package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Airline;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findByIataCode(String iataCode);

    List<Airline> findByCountry(String country);

    List<Airline> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Airline a LEFT JOIN FETCH a.aircrafts WHERE a.id = :id")
    Optional<Airline> findByIdWithAircrafts(Long id);

    @Query("SELECT a FROM Airline a LEFT JOIN FETCH a.flights WHERE a.id = :id")
    Optional<Airline> findByIdWithFlights(Long id);
}
