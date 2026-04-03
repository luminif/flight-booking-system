package ru.java.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.airport.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.passenger WHERE u.id = :id")
    Optional<User> findByIdWithPassenger(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.employee WHERE u.id = :id")
    Optional<User> findByIdWithEmployee(Long id);
}
