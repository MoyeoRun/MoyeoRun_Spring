package com.moyeorun.auth.domain.auth.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshRequest {

  @NotBlank
  private String accessToken;

  @NotBlank
  private String refreshToken;
  
}
