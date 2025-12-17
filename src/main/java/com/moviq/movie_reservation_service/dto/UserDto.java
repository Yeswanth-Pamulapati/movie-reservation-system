package com.moviq.movie_reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviq.movie_reservation_service.model.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

        private long id;
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @Email(message = "Invalid email format")
        @NotBlank(message = "Username cannot be empty")
        private String email;
        @JsonBackReference
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
        private String password;
        private Role role;
        private String status;
        @CreationTimestamp
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        private LocalDateTime createdAt;
        @UpdateTimestamp
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        private LocalDateTime updatedAt;
        // Instead of full Address entity, use AddressDto
        @NotNull(message = "Address cannot be null")
        private List<AddressDto> addresses;
    }

