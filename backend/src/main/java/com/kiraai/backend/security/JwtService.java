package com.kiraai.backend.security;

import	io.jsonwebtoken.Claims;
import	io.jsonwebtoken.Jwts;
import	io.jsonwebtoken.security.Keys;
import	org.springframework.beans.factory.annotation.Value;
import	org.springframework.stereotype.Component;
import	javax.crypto.SecretKey;
import	java.util.Date;

@Component
public	class JwtService {
    @Value("${app.jwt.secret}")
    private	String	secret;
    @Value("${app.jwt.expiration-ms}")
    private	long	expirationMs;
    private	SecretKey	getKey()	{
        return	Keys.hmacShaKeyFor(secret.getBytes());
    }
    public	String	generateToken(String	userId,	String	email)	{
        Date	now	=	new	Date();
        Date	expiry	=	new	Date(now.getTime()	+	expirationMs);
        return	Jwts.builder()
                .subject(userId)
                .claim("email",	email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();
    }
    public	Claims	validateAndParse(String	token)	{
        return	Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}