package com.c8n.userservice.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {
    @Id
    private UUID id;
    private String username;
    private String name;
    private String surname;
    private String identifier;
}
