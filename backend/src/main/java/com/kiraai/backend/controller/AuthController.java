package com.kiraai.backend.controller;

import	com.kiraai.backend.dto.AuthResponse;
import	com.kiraai.backend.dto.GoogleAuthRequest;
import	com.kiraai.backend.service.AuthService;
import	org.springframework.http.ResponseEntity;
import	org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public	class	AuthController	{
    private	final	AuthService	authService;
    public	AuthController(AuthService	authService)	{
        this.authService	=	authService;
    }
    @PostMapping("/google")
    public	ResponseEntity<AuthResponse>	loginWithGoogle(@RequestBody	GoogleAuthRequest	request)	{
        try	{
            AuthResponse	response	=	authService.loginWithGoogle(request.getIdToken());
            return	ResponseEntity.ok(response);
        }	catch	(Exception	e)	{
            return	ResponseEntity.status(401).build();
        }
    }
}
