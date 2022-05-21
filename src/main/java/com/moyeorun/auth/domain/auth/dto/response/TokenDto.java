package com.moyeorun.auth.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {

  private String accessToken;
  private String refreshToken;
}
