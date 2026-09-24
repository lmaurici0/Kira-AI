package com.kiraai.backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    public String generateReply(String userMessage, String systemInstruction, double temperature) {
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                userMessage,
                null
        );

        return response.text();
    }
}
