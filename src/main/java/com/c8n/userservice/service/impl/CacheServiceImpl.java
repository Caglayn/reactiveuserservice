package com.c8n.userservice.service.impl;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.service.CacheService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CacheServiceImpl implements CacheService {
    private final ReactiveValueOperations<String, AuthUser> reactiveValueOps;

    public CacheServiceImpl(ReactiveValueOperations<String, AuthUser> reactiveValueOps) {
        this.reactiveValueOps = reactiveValueOps;
    }

    public Mono<Boolean> updateCacheUser(String key, AuthUser authUser, Duration duration){
        return reactiveValueOps.set(key, authUser, duration);
    }

    public Mono<AuthUser> getCacheUserAndUpdate(String key, Duration duration){
        return reactiveValueOps.getAndExpire(key, duration);
    }

    public Mono<AuthUser> getCacheUser(String key){
        return reactiveValueOps.get(key);
    }
}
