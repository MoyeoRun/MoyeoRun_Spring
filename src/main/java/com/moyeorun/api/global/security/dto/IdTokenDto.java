package com.moyeorun.api.global.security.dto;

import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdTokenDto {

  private String idToken;
  private SnsProviderType providerType;

}
