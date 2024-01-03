package com.c8n.userservice.configuration;

import com.c8n.userservice.model.entity.AuthUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, AuthUser> reactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<AuthUser> valueSerializer = new Jackson2JsonRedisSerializer<>(AuthUser.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, AuthUser> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, AuthUser> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
    }

    @Bean
    public ReactiveValueOperations<String, AuthUser> reactiveValueOps(ReactiveRedisConnectionFactory redisConnectionFactory){
        return reactiveRedisTemplate(redisConnectionFactory).opsForValue();
    }
}
