package com.example.agentdryrun.controller;

import com.example.agentdryrun.dtos.RegisterUserRequest;
import com.example.agentdryrun.entity.Role;
import com.example.agentdryrun.mappers.UserMapper;
import com.example.agentdryrun.repository.UserRepository;
import com.example.agentdryrun.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserMapper userMapper, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest,
                                                            UriComponentsBuilder uriBuilder) {
        if(userRepository.existsByEmail(registerUserRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("email","Email is already in use"));
        }

        var user = userMapper.toEntity(registerUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(userDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);


    }
}
