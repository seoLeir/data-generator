package org.bitpioneers.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
* The RedisConfig class is a configuration class responsible for defining and configuring various components related
 * to the integration of Redis, a popular in-memory data store, into a Spring-based application. This class specifies
 * the connection factory, serializers, and template operations necessary to interact with Redis efficiently.
 * <ul>
 *     <li>
 *         Redis Connection Configuration: It configures the connection to a Redis server using Lettuce as the client
 *         library, specifying the server's host and port.
 *     </li>
 *     <li>
 *         Redis Template Configuration: The class defines a RedisTemplate bean, which serves as a central component
 *         for Redis operations within the application. It sets up the connection factory and customizes the default
 *         serializer for JSON data using the configured ObjectMapper.
 *     </li>
 *     <li>
 *         Redis Template Operations Configuration: The class defines several methods for creating beans that represent
 *         various Redis template operations, such as HyperLogLog, Hash, ZSet, and more. These beans are used to perform
 *         specific operations on Redis data structures.
 *     </li>
 *     <li>
 *         ObjectMapper Configuration: The class provides a method for configuring a default ObjectMapper with features
 *         tailored for JSON serialization and deserialization, including support for Java time types and default typing
 *         information.
 *     </li>
 * </ul>
 *
 * @since 1.0
 * @author Mirolim Mirzayev
*/
@Configuration
public class RedisConfig {


    /**
     * This method defines a RedisConnectionFactory bean that specifies the connection to the Redis server.
     * It utilizes Lettuce as the Redis client and configures a connection to a standalone Redis server
     * running on specific host and port
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }


    /**
    * This method creates and configures a RedisTemplate bean, setting up the connection factory,
     * default serializer, and ObjectMapper for JSON serialization.
     * @see RedisTemplate
     * @param redisConnectionFactory we take from bean RedisFactory
    */
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

    /**
    * This method configuring a default ObjectMapper with features tailored for JSON serialization and deserialization,
     * including support for Java time types and default typing information.
    */
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
