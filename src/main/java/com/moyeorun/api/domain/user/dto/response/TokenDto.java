package com.moyeorun.api.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {

  private String accessToken;
  private String refreshToken;
}
