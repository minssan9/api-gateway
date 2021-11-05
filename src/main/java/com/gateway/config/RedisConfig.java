//package com.gateway.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//
//@Configuration
//@EnableRedisRepositories
//public class RedisConfig {
//
//    @Value("${spring.redis.host}")
//    String host;
//    @Value("${spring.redis.port}")
//    int port;
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//        return template;
//    }
//}


//Redis DB 검토 후 사용
//1. 인증 서버에서 AccessToken 응답시 Redis 저장
//2. 모든 API 요청시 AccessToken 유효성 필터 후 전달
