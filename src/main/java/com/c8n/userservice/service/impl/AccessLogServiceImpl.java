package com.c8n.userservice.service.impl;

import com.c8n.userservice.model.entity.UserAccesslog;
import com.c8n.userservice.repository.sql.UserAccessLogRepository;
import com.c8n.userservice.service.AccessLogService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccessLogServiceImpl implements AccessLogService {
    private final UserAccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(UserAccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public Mono<Boolean> saveUserAccessLog(UserAccesslog accesslog) {
        return accessLogRepository.insertLog(accesslog.getUserId(), accesslog.getIpAddress(), accesslog.getUrl(), accesslog.getDetails())
                .map(rows -> rows > 0).log();
    }
}
