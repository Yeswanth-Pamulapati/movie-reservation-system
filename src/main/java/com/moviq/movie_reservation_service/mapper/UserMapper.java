package com.moviq.movie_reservation_service.mapper;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.dto.UserLoginDto;
import com.moviq.movie_reservation_service.dto.UserRegistrationDto;
import com.moviq.movie_reservation_service.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
    List<UserDto> toDtoList(List<User> users);

//    UserRegistrationDto toRegistrationDto(User user);
//    User toUserEntity(UserRegistrationDto userRegistrationDto);
}
