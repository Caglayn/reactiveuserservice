package com.c8n.userservice.model.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthorityRequestDto {
    private UUID userId;
    private int roleId;
    private String authorityList;
}
