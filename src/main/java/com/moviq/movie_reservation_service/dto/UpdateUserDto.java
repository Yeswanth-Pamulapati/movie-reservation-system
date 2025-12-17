package com.moviq.movie_reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviq.movie_reservation_service.model.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private long id;
    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    private Role role;
    private String status;
    private List<AddressDto> addresses;
}
