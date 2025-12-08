package com.moviq.movie_reservation_service.service;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import com.moviq.movie_reservation_service.mapper.UserMapper;
import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }


    public User registerUser(User user){
        if(userRepo.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
       return userRepo.save(user);//Return the saved record if saved.
    }

    public String login(String username, String password){
        //
        return "Welcome back ";

    }

    public User findUserById(long id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(()->new UserNotFoundException("No user found with id: "+id));
    }

    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(()->new UserNotFoundException("No user found with username: "+username));
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

    public List<UserDto> findAllUsers(){
        return userMapper.toDtoList(userRepo.findAll());
    }


}
