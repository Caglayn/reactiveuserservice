package com.c8n.userservice.service.impl;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.model.entity.User;
import com.c8n.userservice.model.request.LoginRequestDto;
import com.c8n.userservice.model.request.SaveUserRequestDto;
import com.c8n.userservice.repository.sql.UserRepository;
import com.c8n.userservice.service.CacheService;
import com.c8n.userservice.service.TokenService;
import com.c8n.userservice.service.UserAuthorityService;
import com.c8n.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.c8n.userservice.constant.UserConstant.CACHE_USER_DURATION;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final CacheService cacheService;
    private final UserAuthorityService authorityService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService,
                           CacheService cacheService, UserAuthorityService authorityService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
        this.cacheService = cacheService;
        this.authorityService = authorityService;
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
        } else if (saveUserRequestDto != null) {
            return userRepository.findById(saveUserRequestDto.getId())
                    .filter(user -> !user.isDeleted())
                    .flatMap(user -> {
                        user.setName(ObjectUtils.isEmpty(saveUserRequestDto.getName()) ? user.getName() : saveUserRequestDto.getName());
                        user.setSurname(ObjectUtils.isEmpty(saveUserRequestDto.getName()) ? user.getSurname() : saveUserRequestDto.getSurname());
                        user.setPassword(ObjectUtils.isEmpty(saveUserRequestDto.getPassword()) ? user.getPassword() :
                                bCryptPasswordEncoder.encode(saveUserRequestDto.getPassword()));
                        user.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

                        return userRepository.save(user).thenReturn(user);
                    }).map(ResponseEntity.ok()::body)
                    .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
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
    public Mono<ResponseEntity<AuthUser>> loginByUsernameAndPassword(LoginRequestDto dto){
        if (ObjectUtils.isEmpty(dto) || ObjectUtils.isEmpty(dto.getUsername()) || ObjectUtils.isEmpty(dto.getPassword())){
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return userRepository.findByUsername(dto.getUsername())
                .flatMap(user -> {
                    if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())){
                        return tokenService.generateNewToken(user.getId())
                                .flatMap(token -> {
                                    AuthUser authUser = getAuthUserByUser(user);
                                    authUser.setToken(token);
                                    return authorityService.getUserAuthority(authUser.getId())
                                            .flatMap(userAuthority -> {
                                                authUser.setUserAuthority(userAuthority);
                                                return cacheService.updateCacheUser(token, authUser, CACHE_USER_DURATION).then(Mono.just(authUser));
                                            });
                                });
                    } else {
                        return Mono.error(new BadCredentialsException("Bad credentials"));
                    }
                })
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorReturn(ResponseEntity.status(401).body(null));
    }

    @Override
    public Mono<ResponseEntity<Boolean>> logout(String token) {
        return cacheService.removeCacheUser(token)
                .map(ResponseEntity.ok()::body)
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @Override
    public Mono<ResponseEntity<AuthUser>> getUserInfo(AuthUser user) {
        return Mono.just(ResponseEntity.ok().body(user));
    }

    private AuthUser getAuthUserByUser(User user){
        return AuthUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }
}
