package com.moviq.movie_reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @Email(message = "Enter a valid username")
    @NotBlank(message = "Username cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;
}
