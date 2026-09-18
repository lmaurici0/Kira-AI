package com.kiraai.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "message")
public class Message {

    @Id
    private String id;

    @Indexed
    private String conversationId;

    private String role;
    private String content;
    private Integer tokenCount;
    private Instant createdAt;
}