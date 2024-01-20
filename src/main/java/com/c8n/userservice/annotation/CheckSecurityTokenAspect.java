package com.c8n.userservice.annotation;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.c8n.userservice.constant.UserServiceConstant.AUTH_HEADER;
import static com.c8n.userservice.constant.UserServiceConstant.AUTH_USER;

@Slf4j
@Aspect
@Component
@Order(1)
public class CheckSecurityTokenAspect {
    private final CacheService cacheService;

    public CheckSecurityTokenAspect(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(CheckSecurityToken)")
    public Mono<ResponseEntity<?>> checkSecurityToken(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof ServerWebExchange exchange) {
                String token = getAuthHeaderFromRequest(exchange);

                if (token != null) {
                    return cacheService.getCacheUser(token)
                            .flatMap(authUser -> {
                                if (authUser != null) {
                                    exchange.getAttributes().put(AUTH_USER, authUser);
                                    return proceed(joinPoint);
                                } else {
                                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
                                }
                            })
                            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")));
                } else {
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
                }
            }
        }

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
    }

    private Mono<ResponseEntity<Object>> proceed(ProceedingJoinPoint joinPoint){
        try {
            return (Mono<ResponseEntity<Object>>) joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }

    public String getAuthHeaderFromRequest(ServerWebExchange exchange){
        List<String> authHeaders = exchange.getRequest().getHeaders().get(AUTH_HEADER);
        if (authHeaders != null && !authHeaders.isEmpty())
            return authHeaders.getFirst();
        else
            return null;
    }
}
