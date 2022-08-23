package com.moyeorun.api.domain.user.dto.response;

import com.moyeorun.api.domain.user.domain.User;
import lombok.Getter;

@Getter
public class SignUpResponse {

  private TokenDto token;

  private Long userId;

  public SignUpResponse(User user, TokenDto tokenDto) {
    this.token = tokenDto;
    this.userId = user.getId();
  }
}
