package com.moyeorun.api.domain.user.dto.response;

import com.moyeorun.api.domain.user.domain.User;
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
