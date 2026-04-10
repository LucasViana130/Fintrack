package com.projeto.fintrack.mapper;

import com.projeto.fintrack.dto.response.UserResponse;
import com.projeto.fintrack.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
