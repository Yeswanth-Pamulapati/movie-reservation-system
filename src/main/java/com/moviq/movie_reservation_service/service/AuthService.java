package com.moviq.movie_reservation_service.service;

import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenService jwtTokenService;


    public AuthService(AuthenticationManager authManager, JwtTokenService jwtTokenService) {
        this.authManager = authManager;
        this.jwtTokenService = jwtTokenService;
    }

    public String Authenticate(String username, String password) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authManager.authenticate(authToken);// Delegates to DAOAuthentication provider to authenticate
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();// principal contains userdetails
            String token = jwtTokenService.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
            log.info("Token Generated At: , subject={}" , LocalTime.now());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return token;
    }

}
