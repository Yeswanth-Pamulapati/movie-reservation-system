package com.moviq.movie_reservation_service.mapper;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
    List<UserDto> toDtoList(List<User> users);
}
