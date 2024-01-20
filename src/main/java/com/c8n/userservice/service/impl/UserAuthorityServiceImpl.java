package com.c8n.userservice.service.impl;

import com.c8n.userservice.model.entity.UserAuthority;
import com.c8n.userservice.model.request.UserAuthorityRequestDto;
import com.c8n.userservice.repository.sql.UserAuthorityRepository;
import com.c8n.userservice.service.UserAuthorityService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private final UserAuthorityRepository authorityRepository;

    public UserAuthorityServiceImpl(UserAuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Mono<UserAuthority> getUserAuthority(UUID userId) {
        return authorityRepository.getUserAuthorityByUserIdAndDeleted(userId, false);
    }

    @Override
    public Mono<ResponseEntity<Boolean>> addAuthorityToUser(UserAuthorityRequestDto dto) {
        return authorityRepository.insertNewUserAuthority(dto.getUserId(), dto.getRoleId(), dto.getAuthorityList())
                .thenReturn(true)
                .map(ResponseEntity.ok()::body)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @Override
    public Mono<ResponseEntity<Boolean>> updateUserAuthority(UserAuthorityRequestDto dto) {
        //TODO
        return null;
    }

    @Override
    public Mono<ResponseEntity<Boolean>> deleteUserAuthority(UUID userId) {
        return authorityRepository.deleteUserAuthorityByUserId(userId.toString())
                .thenReturn(true)
                .map(ResponseEntity.ok()::body)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
}
