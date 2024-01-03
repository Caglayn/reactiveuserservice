package com.c8n.userservice.service;

import com.c8n.userservice.model.entity.User;
import com.c8n.userservice.model.request.LoginRequestDto;
import com.c8n.userservice.model.request.SaveUserRequestDto;
import com.c8n.userservice.repository.sql.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public interface UserService {
    Mono<ResponseEntity<User>> getUserByUsername(String username);
    Mono<ResponseEntity<User>> saveUser(SaveUserRequestDto saveUserRequestDto);
    Mono<ResponseEntity<Object>> deleteUser(UUID userId);
    Flux<User> getAll();
    Mono<ResponseEntity<String>> loginByUsernameAndPassword(LoginRequestDto dto, ServerHttpRequest request);
}
