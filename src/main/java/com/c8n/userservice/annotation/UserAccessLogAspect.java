package com.c8n.userservice.annotation;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.model.entity.UserAccesslog;
import com.c8n.userservice.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.c8n.userservice.constant.UserServiceConstant.AUTH_USER;

@Slf4j
@Aspect
@Order(2)
public class UserAccessLogAspect {
    private final AccessLogService accessLogService;

    public UserAccessLogAspect(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Around("@annotation(LogUserAccess)")
    public Object logUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof ServerWebExchange exchange) {
                AuthUser user = (AuthUser) exchange.getAttributes().get(AUTH_USER);

                UserAccesslog accessLog = UserAccesslog.builder()
                        .userId(user.getId())
                        .ipAddress(exchange.getRequest().getRemoteAddress() == null ? "" : exchange.getRequest().getRemoteAddress().toString())
                        .url(exchange.getRequest().getURI().toString())
                        .details("")
                        .build();

                return accessLogService.saveUserAccessLog(accessLog)
                        .flatMap(success -> {
                            if (success) {
                                log.info("User access log saved successfully.");
                                // Proceed with the original method execution
                                return Mono.defer(() -> {
                                    try {
                                        return Mono.just(joinPoint.proceed());
                                    } catch (Throwable e) {
                                        log.error("Error during method execution", e);
                                        // Handle the exception appropriately
                                        return Mono.just(ResponseEntity.internalServerError().build());
                                    }
                                });
                            } else {
                                log.error("Failed to save user access log.");
                                // Handle the case where saving the access log failed
                                return Mono.just(ResponseEntity.internalServerError().build());
                            }
                        })
                        .switchIfEmpty(Mono.just(joinPoint.proceed()));
            }
        }

        return Mono.just(joinPoint.proceed());
    }
}
