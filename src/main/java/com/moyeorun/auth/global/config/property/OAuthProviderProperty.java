package com.moyeorun.auth.global.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("oauth")
public class OAuthProviderProperty {

  private String google_client_id;
}
