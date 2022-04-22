package com.moyeorun.auth.domain.auth.dto.request;

import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import lombok.Getter;

@Getter
public class SignUpRequest {

  private String idToken;

  private SnsProviderType providerType;

  private String image;

  private String name;

  private String nickName;

  private Double weight;

  private Double height;

}
