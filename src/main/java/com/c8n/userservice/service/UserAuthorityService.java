package com.c8n.userservice.service;

import com.c8n.userservice.model.entity.UserAuthority;
import com.c8n.userservice.model.request.UserAuthorityRequestDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserAuthorityService {
    Mono<UserAuthority> getUserAuthority(UUID userId);
    Mono<ResponseEntity<Boolean>> addAuthorityToUser(UserAuthorityRequestDto dto);
    Mono<ResponseEntity<Boolean>> updateUserAuthority(UserAuthorityRequestDto dto);
    Mono<ResponseEntity<Boolean>> deleteUserAuthority(UUID userId);
}
