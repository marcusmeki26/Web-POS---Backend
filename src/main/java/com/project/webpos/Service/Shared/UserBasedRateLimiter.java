package com.project.webpos.Service.Shared;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.project.webpos.Exception.LimitReachedException;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserBasedRateLimiter {
  private final LettuceBasedProxyManager<byte[]> lettuceBasedProxyManager;

  /**
   * Performs a check of available tokens that the user can use
   * @param cacheKey a String value of cache key to check
   * @throws LimitReachedException in the case if there is no remaining token to consume
   */
  public void checkToken(String cacheKey, String userId) throws LimitReachedException{
    BucketConfiguration configuration = BucketConfiguration.builder()
        .addLimit(limit -> limit.capacity(100)
            .refillGreedy(100, Duration.ofSeconds(120)))
        .build();

    byte[] keyBytes = cacheKey.getBytes();
    var bucket = lettuceBasedProxyManager.builder().build(keyBytes, () -> configuration);

    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

    if(!probe.isConsumed()){
        long nanosToWait = probe.getNanosToWaitForRefill(); 
        // Convert to seconds
        long retryAfterSeconds = (long) Math.ceil((double) nanosToWait / 1_000_000_000.0);

        LimitReachedException limitReachedException = LimitReachedException.builder()
            .code("TOO_MANY_REQUEST")
            .message("Rate limit exceeded. Please try again later")
            .userId(userId)
            .availableToken(bucket.getAvailableTokens())
            .retryAfter(retryAfterSeconds)
            .build();
        throw limitReachedException;
    }          
  }
}
