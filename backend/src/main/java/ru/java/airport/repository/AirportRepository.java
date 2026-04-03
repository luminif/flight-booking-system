package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Airport;

import java.util.List;
import java.util.Optional;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByIataCode(String iataCode);

    List<Airport> findByCity(String city);

    List<Airport> findByCountry(String country);

    List<Airport> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Airport a LEFT JOIN FETCH a.departingFlights WHERE a.id = :id")
    Optional<Airport> findByIdWithDepartingFlights(Long id);

    @Query("SELECT a FROM Airport a LEFT JOIN FETCH a.arrivingFlights WHERE a.id = :id")
    Optional<Airport> findByIdWithArrivingFlights(Long id);
}
