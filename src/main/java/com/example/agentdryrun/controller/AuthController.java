package com.example.agentdryrun.controller;


import com.example.agentdryrun.config.JwtConfig;
import com.example.agentdryrun.dtos.CredentialsRequest;
import com.example.agentdryrun.dtos.JwtResponse;
import com.example.agentdryrun.mappers.UserMapper;
import com.example.agentdryrun.repository.UserRepository;
import com.example.agentdryrun.service.JwtService;
import com.example.agentdryrun.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginAuth(@Valid @RequestBody CredentialsRequest credentialsRequest,
                                                 HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentialsRequest.getEmail(),
                        credentialsRequest.getPassword()
                )
        );

        var user = userService.getByEmail(credentialsRequest.getEmail());

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/login");
        cookie.setMaxAge((int) jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));


    }

    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken){
        if(!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwtService.getIdfromToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}