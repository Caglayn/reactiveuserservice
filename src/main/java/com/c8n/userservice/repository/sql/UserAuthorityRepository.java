package com.c8n.userservice.repository.sql;

import com.c8n.userservice.model.entity.UserAuthority;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface UserAuthorityRepository extends R2dbcRepository<UserAuthority, UUID> {
    Mono<UserAuthority> getUserAuthorityByUserIdAndDeleted(UUID userId, boolean deleted);

    @Query("INSERT INTO user_authority (id, user_id, user_role_id, user_authority_list, deleted, createdate) " +
            "VALUES (gen_random_uuid(), :userId, :roleId, :authList, FALSE, now())")
    Mono<Void> insertNewUserAuthority(UUID userId, int roleId, String authList);

    @Query("UPDATE user_authority SET deleted=TRUE WHERE user_id=:userId AND deleted=FALSE")
    Mono<Void> deleteUserAuthorityByUserId(String userId);
}
