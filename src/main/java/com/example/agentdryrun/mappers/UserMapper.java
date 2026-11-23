package com.example.agentdryrun.mappers;

import com.example.agentdryrun.dtos.RegisterUserRequest;
import com.example.agentdryrun.dtos.UserDto;
import com.example.agentdryrun.entity.User;

public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);
}
