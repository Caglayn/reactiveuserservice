package com.c8n.userservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.c8n.userservice.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

import static com.c8n.userservice.constant.UserServiceConstant.*;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class TokenServiceImpl implements TokenService {
    private final Algorithm algorithm;

    public TokenServiceImpl(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Mono<String> generateNewToken(UUID userId) {
        try {
            return Mono.just(JWT.create()
                    .withIssuer(AUTH_ISSUER)
                    .withClaim("userId", userId.toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + MONTH_MILIS))
                    .sign(algorithm));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Mono.empty();
        }
    }

    @Override
    public Mono<String> validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(AUTH_ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            return Mono.just(decodedJWT.getClaim("userId").asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Mono.empty();
        }
    }
}
