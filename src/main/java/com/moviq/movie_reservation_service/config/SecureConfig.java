package com.moviq.movie_reservation_service.config;

import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecureConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    //By configuring HttpSecurity, you add, remove, or insert filters at specific positions.
    @Bean
    public SecurityFilterChain SecurityFilter(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/api/users/signup","/api/users/login").permitAll()
                                .requestMatchers("/api/users",
                                "/api/users/id",
                                "/api/users/user-role",
                                "/api/users/by-username",
                                "/api/users/by-reg-date",
                                "/api/users/update-role/{id}",
                                "/api/users/update-status/{id}"
                                        ,"/swagger-ui-custom.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/swagger-config"
                                ).hasAuthority("ADMIN")
                                .requestMatchers("/swagger-ui/index.html").denyAll()
                                .anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }

    //Setting up the password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B,12);
    }
    //Setting the Authentication provider to do the authentication
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider AuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        AuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return AuthenticationProvider;

    }

// Will not let the spring read the username and password from application.properties
    //we can supply the username and password from the code
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("dev")
//                .password("3456")
//                .authorities("ADMIN")
//                .build();// give UserDetailsObject
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("test")
//                .password("1234")
//                .authorities("CUSTOMER")
//                .build();// give UserDetailsObject
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

}
