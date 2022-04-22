package com.moyeorun.auth.global.security.dto;

import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdTokenDto {

  private String idToken;
  private SnsProviderType providerType;

}
