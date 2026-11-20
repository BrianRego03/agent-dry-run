package com.example.agentdryrun.controller;


import com.example.agentdryrun.config.JwtConfig;
import com.example.agentdryrun.mappers.UserMapper;
import com.example.agentdryrun.repository.UserRepository;
import com.example.agentdryrun.service.JwtService;
import com.example.agentdryrun.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.jwtConfig = jwtConfig;
    }
}