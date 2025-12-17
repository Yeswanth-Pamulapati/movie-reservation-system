package com.moviq.movie_reservation_service.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //firstname
    @NotBlank
    private String firstName;
    //lastname
    @NotBlank
    private String lastName;
    //username
    @Email(message = "Invalid email format")
    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true)
    private String email;
    //password
    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false,length = 100)
    private String password;
    //Role
    @Enumerated(EnumType.STRING)
    private Role role;
    //created time
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    //updated time
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private String status;
    //Cascade saves/updates/deletes the addresses automatically in address table when updated in user table.
    //orphanRemoval removes the addresses which are no longer linked to user from the addresses table
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses;


}
