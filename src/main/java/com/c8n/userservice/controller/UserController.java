package com.c8n.userservice.controller;

import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.model.entity.User;
import com.c8n.userservice.model.request.LoginRequestDto;
import com.c8n.userservice.model.request.SaveUserRequestDto;
import com.c8n.userservice.service.CacheService;
import com.c8n.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.c8n.userservice.constant.UserServiceConstant.*;

@RestController
@RequestMapping(API_VERSION + URL_USER)
public class UserController {
    private final UserService userService;

    @Autowired
    private CacheService cacheService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(URL_SAVE)
    public Mono<ResponseEntity<User>> saveUser(@RequestBody SaveUserRequestDto userRequestDto){
        return userService.saveUser(userRequestDto);
    }

    @GetMapping("/getbyusername")
    public Mono<ResponseEntity<User>> getUserByUsername(@RequestParam String username){
        // Test endpoint
        return userService.getUserByUsername(username);
    }

    @DeleteMapping(URL_DELETE)
    public Mono<ResponseEntity<Object>> deleteById(@RequestParam UUID userId){
        return userService.deleteUser(userId);
    }

    @GetMapping("/getall")
    public Flux<User> getAll(){
        // Test endpoint
        return userService.getAll();
    }

    @PostMapping(URL_LOGIN)
    public Mono<ResponseEntity<String>> logInByUsernameAndPassword(@RequestBody LoginRequestDto dto, ServerHttpRequest request){
        return userService.loginByUsernameAndPassword(dto, request);
    }

    @GetMapping("/getbykey")
    public Mono<AuthUser> getFromCacheByKey(@RequestParam String key){
        // Test endpoint
        return cacheService.getCacheUser(key);
    }
}
