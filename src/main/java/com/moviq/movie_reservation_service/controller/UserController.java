package com.moviq.movie_reservation_service.controller;

import com.moviq.movie_reservation_service.dto.UpdateUserDto;
import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.dto.UserLoginDto;
import com.moviq.movie_reservation_service.dto.UserRegistrationDto;
import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.service.AuthService;
import com.moviq.movie_reservation_service.service.JwtTokenService;
import com.moviq.movie_reservation_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.PreUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import jakarta.websocket.server.PathParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(name = "User API")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenService tokenService;

    public UserController(UserService userService, AuthService authService, JwtTokenService tokenService) {
        this.userService = userService;
        this.authService = authService;
        this.tokenService = tokenService;
    }
    @Operation(summary = "Get all the users", description = "Fetches all the existing users")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Find the user by ID", description = "Fetches the user with the provided ID")
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<UserDto> getUserByID(@PathVariable long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Find users by role", description = "Fetches all the users with the provided role")
    @GetMapping("/user-role")
    public ResponseEntity<List<UserDto>> getUserByRole(@RequestParam @NotBlank String role) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUsersByRole(role), HttpStatus.OK);
    }

    @Operation(summary = "Find user by username", description = "Fetches the user with the provided username")
    @GetMapping("/by-username")
    public ResponseEntity<UserDto> getUsersByUsername(@RequestParam
                                                      @Email(message = "Invalid email format")
                                                      @NotBlank(message = "Username cannot be empty") String username) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @Operation(summary = "Find user by registered date", description = "Fetches the user with the provided registered date")
    @GetMapping("/by-reg-date")
    public ResponseEntity<List<UserDto>> getUsersByRegisteredDate(
            @RequestParam
            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate registeredDate) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByRegisteredDate(registeredDate), HttpStatus.OK);
    }

    @Operation(summary = "User Registration", description = "Register a new user with provided details")
    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.registerUser(userDto), HttpStatus.CREATED);
    }

    @Operation(summary = "User Login",description = "Authenticate user when try to login")
    @GetMapping("/login")
    public ResponseEntity<String> userLogin(@Valid @RequestBody UserLoginDto loginRequest){
//       String result = userService.login(loginDto.getEmail(),loginDto.getPassword());
//       return new ResponseEntity<>(result,HttpStatus.OK);
         String token = "token: "+authService.Authenticate(loginRequest.getEmail(),loginRequest.getPassword());
       return new ResponseEntity<>(token, HttpStatus.OK);

    }
    @Operation(summary = "Update user role", description = "Update role of user with provided id")
    @PatchMapping("/update-role/{id}")
    public ResponseEntity<String> updateUserRole(@NotNull @PathVariable long id, @Valid @RequestBody UpdateUserDto update) throws UserNotFoundException {
        int rowsUpdated = userService.updateUserRole(id, update.getRole());
        String result = "Updated the role for the id: "+id +". Rows affected:" + rowsUpdated;
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @Operation(summary = "Update user status", description = "Update status of user with provided id")
    @PatchMapping("/update-status/{id}")
    public ResponseEntity<String> updateUserStatus(@NotNull @PathVariable long id, @RequestBody UpdateUserDto update) throws UserNotFoundException {
        int rowsUpdated = userService.updateUserStatus(id, update.getStatus());
        String result = "Updated the status for the id: "+id +". Rows affected:" + rowsUpdated;
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/sessionId")
    public ResponseEntity<String> getSessionID(HttpServletRequest httpServletRequest){
        String sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(sessionId,HttpStatus.OK);
    }


    }



