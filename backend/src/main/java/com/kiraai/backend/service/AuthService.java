package com.kiraai.backend.service;

import	com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import	com.kiraai.backend.dto.AuthResponse;
import	com.kiraai.backend.model.User;
import	com.kiraai.backend.repository.UserRepository;
import	com.kiraai.backend.security.GoogleTokenValidator;
import	com.kiraai.backend.security.JwtService;
import	org.springframework.stereotype.Service;
import	java.time.Instant;
@Service

public	class	AuthService	{
    private	final	GoogleTokenValidator	googleTokenValidator;
    private	final	UserRepository	userRepository;
    private	final	JwtService	jwtService;
    public	AuthService(GoogleTokenValidator	googleTokenValidator,
                          UserRepository	userRepository,
                          JwtService	jwtService)	{
        this.googleTokenValidator	=	googleTokenValidator;
        this.userRepository	=	userRepository;
        this.jwtService	=	jwtService;
    }
    public	AuthResponse	loginWithGoogle(String	idToken)	throws	Exception	{
        GoogleIdToken.Payload	payload	=	googleTokenValidator.validate(idToken);
        String	googleId	=	payload.getSubject();
        String	email	=	payload.getEmail();
        String	name	=	(String)	payload.get("name");
        String	picture	=	(String)	payload.get("picture");
        User	user	=	userRepository.findByGoogleId(googleId)
                .orElseGet(()	->	{
                    User	newUser	=	new	User();
                    newUser.setGoogleId(googleId);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPictureUrl(picture);
                    newUser.setCreatedAt(Instant.now());
                    return	userRepository.save(newUser);
                });
        String	jwt	=	jwtService.generateToken(user.getId(),	user.getEmail());
        return	new	AuthResponse(jwt,	user.getId(),	user.getName(),	user.getPictureUrl());
    }
}