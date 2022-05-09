package com.moyeorun.auth.domain.auth.dto.request;

import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

  private String idToken;
  private SnsProviderType providerType;
}
