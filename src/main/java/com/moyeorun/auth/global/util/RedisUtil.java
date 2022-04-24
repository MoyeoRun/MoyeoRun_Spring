package com.moyeorun.auth.global.util;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

  private final StringRedisTemplate stringRedisTemplate;

  public void setStringWidthExpire(String key, String value, Long ttl) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    valueOperations.set(key, value);
    stringRedisTemplate.expire(key, ttl, TimeUnit.SECONDS);
  }

  public String getValueByStringKey(String key) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public Boolean deleteByStringKey(String key) {
    return stringRedisTemplate.delete(key);
  }
}
