package com.kiraai.backend.security;

import	com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import	com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import	com.google.api.client.http.javanet.NetHttpTransport;
import	com.google.api.client.json.gson.GsonFactory;
import	org.springframework.beans.factory.annotation.Value;
import	org.springframework.stereotype.Component;
import	java.util.Collections;
@Component

public	class GoogleTokenValidator {
    @Value("${app.google.client-id}")
    private	String	googleClientId;
    public	GoogleIdToken.Payload	validate(String	idTokenString)	throws	Exception	{
        GoogleIdTokenVerifier	verifier	=	new	GoogleIdTokenVerifier.Builder(
                new	NetHttpTransport(),	GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken	idToken	=	verifier.verify(idTokenString);
        if	(idToken	==	null)	{
            throw	new	IllegalArgumentException("ID	Token	invalido	ou	expirado");
        }
        return	idToken.getPayload();
    }
}
