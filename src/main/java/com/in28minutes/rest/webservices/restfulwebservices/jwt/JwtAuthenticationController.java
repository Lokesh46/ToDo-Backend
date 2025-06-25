package com.in28minutes.rest.webservices.restfulwebservices.jwt;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.rest.webservices.restfulwebservices.todo.repository.UserRepository;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;

@RestController
public class JwtAuthenticationController {
    
    private final JwtTokenService tokenService;
    
    private final AuthenticationManager authenticationManager;
    
    private final UserRepository userRepository;
    public JwtAuthenticationController(JwtTokenService tokenService, 
            AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) {
        
        var authenticationToken = 
                new UsernamePasswordAuthenticationToken(
                        jwtTokenRequest.username(), 
                        jwtTokenRequest.password());
        
        var authentication = 
                authenticationManager.authenticate(authenticationToken);
        
        var token = tokenService.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody JwtTokenRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByUsername(registerRequest.username());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body("Username already exists.");
        }

        // You should encode the password in production. For now using plain-text.
        User newUser = new User();
        newUser.setUsername(registerRequest.username());
        newUser.setPassword(registerRequest.password()); // 👉 Replace with encoded password if needed
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully.");
    }
}


