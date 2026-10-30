package com.project.webpos.Configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration 
public class RedisCacheConfig {
  /**
   * Creates the configuration of Redis caching inside the application
   * @return an object of RedisCacheConfigurtation
   */
  @Bean 
  public RedisCacheConfiguration redisCacheConfiguration(){
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(10))
        .disableCachingNullValues()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.
              fromSerializer(RedisSerializer.json()));
  }
  
  /**
   * Creates a configuration on how to serialize strings and objects
   * @param connectionFactory an object of {@code RedisConnectionFactory}
   * @return
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
      RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setConnectionFactory(connectionFactory);

      template.setKeySerializer(RedisSerializer.string());
      template.setHashKeySerializer(RedisSerializer.string());
      
      template.setValueSerializer(RedisSerializer.json());
      template.setHashValueSerializer(RedisSerializer.json());
      
      template.afterPropertiesSet();

      return template;
  }
}
