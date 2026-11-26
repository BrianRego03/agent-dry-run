package com.example.agentdryrun.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.agentdryrun.dtos.ChangePasswordRequest;
import com.example.agentdryrun.dtos.RegisterUserRequest;
import com.example.agentdryrun.entity.Role;
import com.example.agentdryrun.mappers.UserMapper;
import com.example.agentdryrun.repository.UserRepository;
import com.example.agentdryrun.service.UserService;

import jakarta.validation.Valid;

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

    @GetMapping("/test")
    public String hello() {
        return "Hello! Welcome to my Spring Boot application.";
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

    @PostMapping("/users/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable int id,
                                            @RequestBody ChangePasswordRequest passwordRequest){
        var user = userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        String newPassword = passwordRequest.getNewPassword();
        if(newPassword==null || newPassword.isBlank()){
            return ResponseEntity.badRequest()
                    .body(Map.of("newPassword","New password must not be blank"));
        }
        if(!(passwordEncoder.matches(passwordRequest.getOldPassword(),user.getPassword()))){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
