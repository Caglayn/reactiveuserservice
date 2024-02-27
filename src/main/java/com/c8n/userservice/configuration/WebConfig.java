package com.c8n.userservice.configuration;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.c8n.userservice.constant.UserServiceConstant.ALLOWED_ORIGINS;

@Configuration
public class WebConfig {
    @Value("${secret_key}")
    private String AUTH_SECRET;
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Algorithm algorithm(){
        return Algorithm.HMAC256(AUTH_SECRET);
    }

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            if (CorsUtils.isCorsRequest(ctx.getRequest())) {
                List<String> originHeaders = ctx.getRequest().getHeaders().get("Origin");
                if (originHeaders != null && !originHeaders.isEmpty() && ALLOWED_ORIGINS.contains(originHeaders.getFirst())){
                    ctx.getResponse().getHeaders().add("Access-Control-Allow-Origin", originHeaders.getFirst());
                    ctx.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                    ctx.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                    ctx.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
                }

                if (ctx.getRequest().getMethod() == HttpMethod.OPTIONS) {
                    ctx.getResponse().setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }
}
