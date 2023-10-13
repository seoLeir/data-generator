package org.bitpioneers.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(makeDefaultObjectMapper()));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public <K, V> HyperLogLogOperations<K, V> hyperLogLogOperations(RedisTemplate<K, V> template) {
        return template.opsForHyperLogLog();
    }

    @Bean
    public <K, HK, V> HashOperations<K, HK, V> hashOperations(RedisTemplate<K, V> template) {
        return template.opsForHash();
    }

    @Bean
    public <K, V> ZSetOperations<K, V> zSetOperations(RedisTemplate<K, V> template) {
        return template.opsForZSet();
    }

    @Bean
    public <K, V> ClusterOperations<K, V> clusterOperations(RedisTemplate<K, V> template) {
        return template.opsForCluster();
    }

    @Bean
    public <K, V> GeoOperations<K, V> geoOperations(RedisTemplate<K, V> template) {
        return template.opsForGeo();
    }

    @Bean
    public <K, V> ListOperations<K, V> listOperations(RedisTemplate<K, V> template) {
        return template.opsForList();
    }

    @Bean
    public <K, V> SetOperations<K, V> setOperations(RedisTemplate<K, V> template) {
        return template.opsForSet();
    }

    @Bean
    public <K, HK, V> StreamOperations<K, HK, V> streamOperations(RedisTemplate<K, V> template) {
        return template.opsForStream();
    }

    @Bean
    public <K, V> ValueOperations<K, V> valueOperations(RedisTemplate<K, V> template) {
        return template.opsForValue();
    }

    private static ObjectMapper makeDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }
}
