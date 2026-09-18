package com.kiraai.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "user")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String googleId;

    @Indexed(unique = true)
    private String email;

    private String name;
    private String pictureUrl;
    private Instant createdAt;
}