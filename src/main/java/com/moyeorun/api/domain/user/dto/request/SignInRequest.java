package com.moyeorun.api.domain.user.dto.request;

import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

  private String idToken;
  private SnsProviderType providerType;
}
