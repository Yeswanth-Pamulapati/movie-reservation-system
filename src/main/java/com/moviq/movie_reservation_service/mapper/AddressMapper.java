package com.moviq.movie_reservation_service.mapper;

import com.moviq.movie_reservation_service.dto.AddressDto;
import com.moviq.movie_reservation_service.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
//    @Mapping(source = "user.id", target = "userId")
    AddressDto toDto(Address address);

//    @Mapping(source = "userId", target = "user.id")
Address toEntity(AddressDto dto);
}
