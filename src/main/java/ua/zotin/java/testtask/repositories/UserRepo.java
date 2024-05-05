package ua.zotin.java.testtask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.zotin.java.testtask.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByBirthDateBetween(LocalDate from, LocalDate to);
}
