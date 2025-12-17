package com.moviq.movie_reservation_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Street cannot be blank")
    @Column(length = 30)
    @Size(max = 30, message = "Street must be less than 30 characters")
    private String street;
    @Column(length = 30)
    @NotBlank(message = "City cannot be blank")
    @Size(max = 30, message = "City must be less than 30 characters")
    private String city;
    @Column(length = 30)
    @NotBlank(message = "State cannot be blank")
    @Size(max = 30, message = "State must be less than 31 characters")
    private String state;
    @Column(length = 30)
    @NotBlank(message = "Country cannot be blank")
    @Size(max = 30, message = "Country must be less than 31 characters")
    private String country;
    @Column(length = 6)
    @NotBlank(message = "Zip code cannot be blank")
    @Pattern(regexp = "^[0-9]{6}$", message = "Zip code must be exactly 6 digits")
    private String zipCode;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    @ToString.Exclude
    private User user;

}
