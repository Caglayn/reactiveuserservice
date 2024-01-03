package com.c8n.userservice.repository.sql;

import com.c8n.userservice.model.entity.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface UserRepository extends R2dbcRepository<User, UUID> {
    Mono<User> findByUsername(String username);
    @Query("INSERT INTO user_record (id, username, password, name, surname, identifier, createdate, deleted) " +
            "VALUES (gen_random_uuid(), :username, :password, :name, :surname, :identifier, now(), FALSE)")
    Mono<User> insertNewUser(String username, String password, String name, String surname, String identifier);

    @Query("UPDATE user_record SET updatedate=now(), deleted=TRUE WHERE id=:userId")
    Mono<Void> deleteUserById(UUID userId);
}
