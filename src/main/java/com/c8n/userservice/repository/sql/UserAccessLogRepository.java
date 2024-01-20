package com.c8n.userservice.repository.sql;

import com.c8n.userservice.model.entity.UserAccesslog;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface UserAccessLogRepository extends R2dbcRepository<UserAccesslog, UUID> {
    @Query("INSERT INTO user_access_log (id, user_id, createdate, ip_address, url, details) " +
    "VALUES (gen_random_uuid(), :userId, now(), :ipAddress, :url, :details)")
    Mono<Integer> insertLog(UUID userId, String ipAddress, String url, String details);
}
