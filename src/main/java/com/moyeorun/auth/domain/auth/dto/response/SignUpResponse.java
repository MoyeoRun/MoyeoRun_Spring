package com.moyeorun.auth.domain.auth.dto.response;

import com.moyeorun.auth.domain.auth.domain.User;
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
