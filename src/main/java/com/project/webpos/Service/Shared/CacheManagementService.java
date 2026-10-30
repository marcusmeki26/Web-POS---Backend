package com.project.webpos.Service.Shared;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CacheManagementService {
  // Should contain retrieving, adding, and deleting of cache
  private final RedisTemplate<String, Object> redisTemplate;

  public void addKey(String key, Object value, Duration ttl){
    if(ttl == null)
      redisTemplate.opsForValue().set(key, value);
    else
      redisTemplate.opsForValue().set(key, value, ttl);
  }

  public Object getValueOfKey(String key){
    return redisTemplate.opsForValue().get(key);
  }

  public void deleteKey(){

  }

  public void deleteKeyByPatter(){

  }
}
