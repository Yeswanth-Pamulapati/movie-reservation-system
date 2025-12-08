package com.moviq.movie_reservation_service.controller;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import com.moviq.movie_reservation_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import jakarta.websocket.server.PathParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<UserDto> getUserByID(@PathVariable long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
    }

    @GetMapping("/user-role")
    public ResponseEntity<List<UserDto>> getUserByRole(@RequestParam @NotBlank String role) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUsersByRole(role),HttpStatus.OK);
    }

    @GetMapping("/by-username")
    public ResponseEntity<UserDto> getUsersByUsername(@RequestParam
                         @Email(message = "Invalid email format")
                         @NotBlank(message = "Username cannot be empty") String username) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByUsername(username),HttpStatus.OK);
    }
    @GetMapping("/by-reg-date")
    public ResponseEntity<List<UserDto>> getUsersByRegisteredDate(
                                                            @RequestParam
                                                            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate registeredDate) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByRegisteredDate(registeredDate),HttpStatus.OK);
    }
    }


