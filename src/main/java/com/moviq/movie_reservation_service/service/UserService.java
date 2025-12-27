package com.moviq.movie_reservation_service.service;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.dto.UserRegistrationDto;
import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import com.moviq.movie_reservation_service.exception.UsernameAlreadyExistsException;
import com.moviq.movie_reservation_service.mapper.UserMapper;
import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.model.UserStatus;
import com.moviq.movie_reservation_service.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, UserMapper userMapper,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder=passwordEncoder;
    }

    @Transactional
    public String registerUser(UserDto userRegDto){
        User user = userMapper.toEntity(userRegDto);
        //applying the BCrypt encryption to the password.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepo.existsByEmail(user.getEmail())){
            throw new UsernameAlreadyExistsException("Email already exists");
        }
        if(user.getRole()==null) {
            user.setRole(Role.CUSTOMER);
        }
        if(user.getStatus()==null) {
            user.setStatus(UserStatus.ACTIVE.name());
        }
        User savedUser = userRepo.save(user);//Returns the saved record if saved.
        return "User Registered Successfully" ;
    }
//
//    public String login(String username, String password){
//        User user = userRepo.findByEmail(username)
//                .orElseThrow(()-> new UsernameNotFoundException(
//                        "No User found with username "+ username+". Please check the username provided"));
//        if(!passwordEncoder.matches(password,user.getPassword())){
//            throw new BadCredentialsException("Invalid Password.");
//        }
//        return "Welcome back " + user.getFirstName() +" "+ user.getLastName();
//
//    }

    public UserDto findUserById(long id) throws UserNotFoundException {

           User user = userRepo.findById(id).orElseThrow(()->new UserNotFoundException("No user found with id: "+id));
           return userMapper.toDto(user);

    }

    public UserDto findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepo.findByEmail(username).orElseThrow(()->new UserNotFoundException("No user found with username: "+username));
        return userMapper.toDto(user);
    }

    public List<UserDto> findUsersByRole(String role) throws UserNotFoundException {
        Role enumRole;
        try {
            enumRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        List<User>users = userRepo.findByRole(enumRole);
        if(users.isEmpty()){
            throw new UserNotFoundException("No users found with role: " + role);
        }
        return userMapper.toDtoList(users);

    }

    public List<UserDto> findUsersByStatus(String status) throws UserNotFoundException {
        UserStatus enumStatus;
        try {
            enumStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + status);
        }
        List<User>users = userRepo.findByStatus(enumStatus.name());
        if(users.isEmpty()){
            throw new UserNotFoundException("No users found with role: " + status);
        }
        return userMapper.toDtoList(users);

    }


    public List<UserDto> findAllUsers(){
        return userMapper.toDtoList(userRepo.findAll());
    }


    public List<UserDto> findUserByRegisteredDate(LocalDate registeredDate) {
        LocalDateTime start = registeredDate.atStartOfDay();
        LocalDateTime end = registeredDate.atTime(LocalTime.MAX);
        return userMapper.toDtoList(userRepo.findByCreatedAtBetween(start,end));
    }
    @Transactional
    public int updateUserRole(long id, Role newRole) throws UserNotFoundException {
        if(!userRepo.existsById(id)) {
            throw new UserNotFoundException("No user exists with id: " + id);
        }
        int rowsUpdated= userRepo.updateUserRole(id,newRole);
        User user = userRepo.findById(id).orElse(new User());
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
        return rowsUpdated;
    }

    @Transactional
    public int updateUserStatus(long id, String status) throws UserNotFoundException {
        if(!userRepo.existsById(id)) {
            throw new UserNotFoundException("No user exists with id: " + id);
        }
        UserStatus userStatus;
        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        return userRepo.updateUserStatus(id,userStatus.name());
    }
}
