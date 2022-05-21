package com.moyeorun.auth.domain.auth.dto.response;

import com.moyeorun.auth.domain.auth.domain.User;
import lombok.Getter;

@Getter
public class SignInResponse {

  private TokenDto token;
  private Boolean isNewUser;
  private Long userId;

  public SignInResponse(User user, TokenDto tokenDto) {
    this.isNewUser = false;
    this.userId = user.getId();
    this.token = tokenDto;
  }

  public SignInResponse() {
    this.isNewUser = true;
  }
}
