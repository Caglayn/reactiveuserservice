package com.c8n.userservice.controller;

import com.c8n.userservice.annotation.CheckSecurityToken;
import com.c8n.userservice.model.entity.AuthUser;
import com.c8n.userservice.model.entity.User;
import com.c8n.userservice.model.request.LoginRequestDto;
import com.c8n.userservice.model.request.SaveUserRequestDto;
import com.c8n.userservice.service.CacheService;
import com.c8n.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
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
    public Mono<ResponseEntity<User>> saveUser(@RequestBody SaveUserRequestDto userRequestDto, ServerWebExchange request){
        return userService.saveUser(userRequestDto);
    }

    @DeleteMapping(URL_DELETE)
    public Mono<ResponseEntity<Object>> deleteById(@RequestParam UUID userId, ServerWebExchange request){
        return userService.deleteUser(userId);
    }

    @PostMapping(URL_LOGIN)
    public Mono<ResponseEntity<AuthUser>> logInByUsernameAndPassword(@RequestBody LoginRequestDto dto, ServerWebExchange request){
        return userService.loginByUsernameAndPassword(dto);
    }

    @CheckSecurityToken
    @GetMapping(URL_INFO)
    public Mono<ResponseEntity<AuthUser>> getUserInfo(ServerWebExchange request){
        AuthUser authUser = request.getAttribute(AUTH_USER);
        return userService.getUserInfo(authUser);
    }

    @CheckSecurityToken
    @GetMapping(URL_LOGOUT)
    public Mono<ResponseEntity<Boolean>> logout(ServerWebExchange request){
        AuthUser authUser = request.getAttribute(AUTH_USER);
        return userService.logout(authUser == null ? "" : authUser.getToken());
    }

    public Mono<ResponseEntity<String>> pingPong(){
        return Mono.just(ResponseEntity.ok("pong"));
    }
}
