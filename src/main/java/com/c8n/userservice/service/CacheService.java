package com.c8n.userservice.service;

import com.c8n.userservice.model.entity.AuthUser;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface CacheService {
    Mono<Boolean> updateCacheUser(String key, AuthUser authUser, Duration duration);
    Mono<AuthUser> getCacheUserAndUpdate(String key, Duration duration);
    Mono<AuthUser> getCacheUser(String key);
}
