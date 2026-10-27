package com.project.webpos.AnnotationAspectClass;

import java.time.Duration;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.webpos.AnnotationInterface.RateLimited;
import com.project.webpos.Exception.LimitReachedException;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect 
@Component 
@RequiredArgsConstructor 
public class RateLimiterAspect {
  private final LettuceBasedProxyManager<byte[]> lettuceBasedProxyManager;

  @Before("@annotation(rateLimited)")
  public void beforeRequest(RateLimited rateLimited){
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String ipAddress = request.getRemoteAddr();
    String endpoint = request.getRequestURI();
    String cacheKey = "rate_limit:" + endpoint + ":" + ipAddress;

    BucketConfiguration configuration = BucketConfiguration.builder()
        .addLimit(limit -> limit.capacity(rateLimited.capacity())
            .refillGreedy(rateLimited.refillTokens(), Duration.ofSeconds(rateLimited.periodInSeconds())))
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
            .ipAddress(ipAddress)
            .endpoint(endpoint)
            .message("Rate limit exceeded. Please try again later")
            .availableToken(bucket.getAvailableTokens())
            .retryAfter(retryAfterSeconds)
            .build();
        throw limitReachedException;
    }
  }
}
