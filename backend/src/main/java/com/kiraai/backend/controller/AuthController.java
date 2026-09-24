package com.kiraai.backend.controller;

import	com.kiraai.backend.dto.AuthResponse;
import	com.kiraai.backend.dto.GoogleAuthRequest;
import com.kiraai.backend.model.User;
import com.kiraai.backend.repository.UserRepository;
import	com.kiraai.backend.service.AuthService;
import	org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import	org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public	class	AuthController	{
    private	final	AuthService	authService;
    public	AuthController(AuthService	authService)	{
        this.authService	=	authService;
    }

    private UserRepository userRepository;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleAuthRequest request) {
        try {
            AuthResponse response = authService.loginWithGoogle(request.getIdToken());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        return ResponseEntity.ok(new AuthResponse(null, user.getId(), user.getName(), user.getPictureUrl()));
    }
}
