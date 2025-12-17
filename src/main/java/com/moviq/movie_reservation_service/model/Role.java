package com.moviq.movie_reservation_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    CUSTOMER;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.replace("\"", "").toUpperCase());
    }
}
