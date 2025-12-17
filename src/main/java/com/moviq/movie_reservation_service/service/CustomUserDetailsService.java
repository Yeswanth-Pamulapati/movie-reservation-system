package com.moviq.movie_reservation_service.service;

import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("No user found with username: "+username));

        return new CustomUserDetails(user);
    }
}
