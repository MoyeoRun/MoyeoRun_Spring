package com.moyeorun.api.domain.user.dto.request;

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
