package com.c8n.userservice.controller;

import com.c8n.userservice.model.request.UserAuthorityRequestDto;
import com.c8n.userservice.service.UserAuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.c8n.userservice.constant.UserServiceConstant.*;

@RestController
@RequestMapping()
public class UserAuthorityController {
    private final UserAuthorityService userAuthorityService;

    public UserAuthorityController(UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    @PostMapping(URL_SAVE)
    public Mono<ResponseEntity<Boolean>> saveUserAuthority(UserAuthorityRequestDto dto){
        return userAuthorityService.addAuthorityToUser(dto);
    }

    @DeleteMapping(URL_DELETE)
    public Mono<ResponseEntity<Boolean>> deleteUserAuthority(UUID userId){
        return userAuthorityService.deleteUserAuthority(userId);
    }
}
