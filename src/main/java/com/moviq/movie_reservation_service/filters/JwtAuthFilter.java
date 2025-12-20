package com.moviq.movie_reservation_service.filters;

import com.moviq.movie_reservation_service.service.CustomUserDetailsService;
import com.moviq.movie_reservation_service.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtTokenService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          String header =   request.getHeader("Authorization");
          if (header!=null && header.startsWith("Bearer ")) {
              String token = header.substring(7);
              if (jwtService.isValid(token) &&  SecurityContextHolder.getContext().getAuthentication()==null) {
                  String username = jwtService.getUsername(token);
                  UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                  UsernamePasswordAuthenticationToken auth =
                          new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                  auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(auth);

              }

          }
          filterChain.doFilter(request,response);
    }
}
