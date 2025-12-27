package com.moviq.movie_reservation_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long id;
    @NotBlank
    @Size(max = 255)
    private String title;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Min(1) // represents 60 mins
    @Max(600)
    private int durationMinutes;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{2}$")//ISO 639‑1 Standard
    private List<String> languages;
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double rating;
    @NotNull
    private LocalDate releaseDate;
    @CreationTimestamp
    private LocalDateTime createdAt;


}

