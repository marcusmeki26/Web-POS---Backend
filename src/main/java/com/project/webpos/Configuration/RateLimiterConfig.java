package com.project.webpos.Configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;

@Configuration 
public class RateLimiterConfig {
  @Bean(destroyMethod = "shutdown")
  public RedisClient redisClient(
    @Value("${SPRING.DATA.REDIS.URL}")
    String redisUrl
  ) {
    return RedisClient.create(redisUrl);
  }

  @Bean
  public LettuceBasedProxyManager<byte[]> proxyManager(RedisClient redisClient){
    ClientSideConfig clientSideConfig = ClientSideConfig.getDefault()
        .withExpirationAfterWriteStrategy(
          ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofHours(3))
        );

    return LettuceBasedProxyManager.builderFor(redisClient)
        .withClientSideConfig(clientSideConfig)
        .build();
  }
}
