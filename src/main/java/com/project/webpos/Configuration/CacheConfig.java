package com.project.webpos.Configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration 
public class CacheConfig {
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
}
