package com.moviq.movie_reservation_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Username cannot be empty")
        @Column(unique = true)
        private String email;

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
        )
        private String password;
        @NotNull
        private List<AddressDto> addresses;
}
