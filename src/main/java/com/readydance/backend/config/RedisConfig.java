package com.readydance.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;  //Todo redis host 변경 (현재 localhost)

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}

//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    @Value("${spring.redis.password}")
//    private String redisPassword;
//
//    private RedisProperties redisProperties;
//
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//      //  return new LettuceConnectionFactory(redisProperties.getHost(),redisProperties.getPort());
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setPort(redisPort);
//        redisStandaloneConfiguration.setPassword(redisPassword);
//
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//        return lettuceConnectionFactory;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        // 표준화 된 하나의 DTO만 사용할 경우 redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisVO.class)); 를 사용
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 여러 DTO가 가능
//        // @class데이터가 붙어서 Serializer를 통해 객체로 변경해줄 때 어떤 DTO로 매핑할 지 체크한다.
//        // {"@class":"com.example.demo.common.redis.RedisVO","key1":"value1","key2":"value2"}
//        return redisTemplate;
//    }
