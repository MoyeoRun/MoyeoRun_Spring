package com.moyeorun.auth.domain.auth.dto.request;

import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import lombok.Getter;

@Getter
public class SignInRequest {

  private String idToken;
  private SnsProviderType providerType;
}
