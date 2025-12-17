package com.moviq.movie_reservation_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info().title("Movie Reservation Service API")
                .version("1.0")
                .description("API documentation for Movie Reservation backend")
                .contact(new Contact().name("Yeswanth").email("test@mail.com")));


    }


}
