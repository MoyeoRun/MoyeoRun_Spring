package com.moyeorun.api.domain.user.dto.response;

import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.domain.user.domain.contant.GenderType;
import lombok.Getter;

@Getter
public class UserResponse {

  private Long userId;

  private String name;

  private String nickName;

  private String email;

  private GenderType gender;

  private String image;

  public UserResponse(User user) {
    this.userId = user.getId();
    this.name = user.getName();
    this.nickName = user.getNickName();
    this.email = user.getEmail();
    this.gender = user.getGender();
    this.image = user.getImage();
  }
}
