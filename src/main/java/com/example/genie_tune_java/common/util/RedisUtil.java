package com.example.genie_tune_java.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
  private final RedisTemplate<String, Object> redisTemplate;

  public void set(String key, String value, long timeoutMillis) {
    if(timeoutMillis > 0) {
      redisTemplate.opsForValue().set(key, value, timeoutMillis, TimeUnit.MILLISECONDS);
    } else {
      redisTemplate.opsForValue().set(key, value);
    }
  }

  public String get(String key) {
    Object value = redisTemplate.opsForValue().get(key);
    return value != null ? value.toString() : null;
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }

  public boolean hasKey(String key) {
    return redisTemplate.hasKey(key);
  }

  public long getTtl(String key) {
    return redisTemplate.getExpire(key);
  }
}
