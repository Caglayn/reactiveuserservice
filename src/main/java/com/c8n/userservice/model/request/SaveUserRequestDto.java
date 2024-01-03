package com.c8n.userservice.model.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveUserRequestDto {
    private UUID id;
    private String name;
    private String surname;
    private String identifier;
    private String password;
    private String username;
}
