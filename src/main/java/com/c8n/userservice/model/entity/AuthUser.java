package com.c8n.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 7665533609728323378L;
    @Id
    private UUID id;
    private String username;
    private String name;
    private String surname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserAuthority userAuthority;
}
