package com.c8n.userservice.model.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    private String userName;
    private String password;
}
