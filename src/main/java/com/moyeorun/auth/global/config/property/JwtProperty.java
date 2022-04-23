package com.moyeorun.auth.global.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("jwt")
public class JwtProperty {

  private String secret_key;
  private Long access_token_expired_time;
  private Long refresh_token_expired_time;
}
