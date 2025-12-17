package com.moviq.movie_reservation_service.repository;

import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    //Update the user role
    @Modifying //required for update/delete queries in Spring Data JPA.
    @Query("UPDATE User u SET u.role = :newRole where u.id = :id")
    int updateUserRole(@Param("id") long id, @Param("newRole") Role newRole ); // returns number of rows updated.

    //update the user status
    @Modifying
    @Query("UPDATE User u SET u.status = :status where u.id = :id")
    int updateUserStatus(@Param("id") long id, @Param("status") String status ); // returns number of rows updated.
}
