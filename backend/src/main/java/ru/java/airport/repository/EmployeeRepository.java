package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);

    List<Employee> findByPosition(String position);

    List<Employee> findByLastName(String lastName);

    Optional<Employee> findByLicenseNumber(String licenseNumber);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.crewAssignments WHERE e.id = :id")
    Optional<Employee> findByIdWithCrewAssignments(Long id);

    @Query("""
            SELECT e FROM Employee e
            LEFT JOIN e.crewAssignments ca
            WHERE ca.flight.id = :flightId
            """)
    List<Employee> findByFlightId(Long flightId);
}
