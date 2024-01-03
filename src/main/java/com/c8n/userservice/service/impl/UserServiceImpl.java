package com.c8n.userservice.service.impl;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.model.entity.User;
import com.c8n.userservice.model.request.LoginRequestDto;
import com.c8n.userservice.model.request.SaveUserRequestDto;
import com.c8n.userservice.repository.sql.UserRepository;
import com.c8n.userservice.service.CacheService;
import com.c8n.userservice.service.TokenService;
import com.c8n.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final CacheService cacheService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService, CacheService cacheService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
        this.cacheService = cacheService;
    }

    @Override
    public Mono<ResponseEntity<User>> getUserByUsername(String username){
        if (ObjectUtils.isEmpty(username))
            return Mono.just(ResponseEntity.badRequest().build());
        else
            return userRepository.findByUsername(username).map(ResponseEntity.ok()::body);
    }

    @Override
    public Mono<ResponseEntity<User>> saveUser(SaveUserRequestDto saveUserRequestDto){
        if (saveUserRequestDto != null && saveUserRequestDto.getId() == null){
            saveUserRequestDto.setPassword(bCryptPasswordEncoder.encode(saveUserRequestDto.getPassword()));
            return userRepository.insertNewUser(saveUserRequestDto.getUsername(), saveUserRequestDto.getPassword(), saveUserRequestDto.getName(),
                            saveUserRequestDto.getSurname(), saveUserRequestDto.getIdentifier())
                    .map(ResponseEntity.ok()::body)
                    .onErrorReturn(ResponseEntity.badRequest().build());
        }
        else if (saveUserRequestDto != null) {
            User user = User.builder()
                    .id(saveUserRequestDto.getId())
                    .name(saveUserRequestDto.getName())
                    .surname(saveUserRequestDto.getSurname())
                    .username(saveUserRequestDto.getUsername())
                    .identifier(saveUserRequestDto.getIdentifier())
                    .password(saveUserRequestDto.getPassword())
                    .updateDate(new Timestamp(new Date().getTime()))
                    .build();

            return userRepository.save(user)
                    .map(ResponseEntity.ok()::body)
                    .switchIfEmpty(Mono.just(ResponseEntity.internalServerError().build()))
                    .onErrorReturn(ResponseEntity.notFound().build());
        } else
            return Mono.just(ResponseEntity.badRequest().build());
    }

    @Override
    public Mono<ResponseEntity<Object>> deleteUser(UUID userId){
        if (ObjectUtils.isEmpty(userId)) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else {
            return userRepository.findById(userId).log()
                    .flatMap(user -> userRepository.deleteUserById(user.getId()).then()
                            .thenReturn(ResponseEntity.ok().build()))
                    .doOnSuccess(responseEntity -> log.info("User deleted : " + userId));
        }
    }

    @Override
    public Flux<User> getAll() {
        return userRepository.findAll().log();
    }

    @Override
    public Mono<ResponseEntity<String>> loginByUsernameAndPassword(LoginRequestDto dto, ServerHttpRequest request){
        if (ObjectUtils.isEmpty(dto) || ObjectUtils.isEmpty(dto.getUserName()) || ObjectUtils.isEmpty(dto.getPassword())){
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return userRepository.findByUsername(dto.getUserName())
                .flatMap(user -> {
                    if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())){
                        return tokenService.generateNewToken(user.getId())
                                .flatMap(token -> cacheService.updateCacheUser(token, getAuthUserByUser(user), Duration.ofDays(1)).then().thenReturn(token));
                    } else {
                        return Mono.error(new BadCredentialsException("Bad credentials"));
                    }
                })
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorReturn(ResponseEntity.status(401).body("Bad credentials"));
    }

    private AuthUser getAuthUserByUser(User user){
        return AuthUser.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .identifier(user.getIdentifier())
                .build();
    }
}
