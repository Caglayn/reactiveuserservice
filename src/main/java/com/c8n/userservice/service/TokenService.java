package com.c8n.userservice.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TokenService {
    Mono<String> generateNewToken(UUID userId);
    Mono<String> validateToken(String token);
}
