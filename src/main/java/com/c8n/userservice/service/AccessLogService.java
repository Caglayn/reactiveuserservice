package com.c8n.userservice.service;

import com.c8n.userservice.model.entity.UserAccesslog;
import reactor.core.publisher.Mono;

public interface AccessLogService {
    Mono<Boolean> saveUserAccessLog(UserAccesslog accesslog);
}
