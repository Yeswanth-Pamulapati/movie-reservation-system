package com.moviq.movie_reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviq.movie_reservation_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
        private String status;
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        private LocalDateTime updatedAt;
        // Instead of full Address entity, use AddressDto
        private List<AddressDto> addresses;
    }

