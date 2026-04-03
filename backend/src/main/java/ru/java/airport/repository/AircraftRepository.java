package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Aircraft;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByRegistrationNumber(String registrationNumber);

    List<Aircraft> findByModel(String model);

    List<Aircraft> findByAirlineId(Long airlineId);

    List<Aircraft> findByCapacityGreaterThan(Integer capacity);

    @Query("SELECT a FROM Aircraft a LEFT JOIN FETCH a.flights WHERE a.id = :id")
    Optional<Aircraft> findByIdWithFlights(Long id);

    @Query("SELECT a FROM Aircraft a LEFT JOIN FETCH a.flights WHERE a.airline.id = :airlineId")
    List<Aircraft> findByAirlineIdWithFlights(Long airlineId);
}
