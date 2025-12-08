package com.moviq.movie_reservation_service.repository;

import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    //find user by username or email
    Optional<User> findByEmail(String email);
    //find user by id
    Optional<User> findById(long id);
    //find user by role
    List<User> findByRole(Role role);
    // Find users by status (e.g., ACTIVE, INACTIVE)
    List<User> findByStatus(String status);
    // Search by first or last name (basic filtering)
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    boolean existsByEmail(String email);
    //Find by registered date
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
