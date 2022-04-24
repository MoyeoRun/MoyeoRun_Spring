package com.moyeorun.auth.domain.auth.dto.response;

import com.moyeorun.auth.domain.auth.domain.User;
import lombok.Getter;

@Getter
public class UserResponse {

  private Long userId;

  private String name;

  private String nickName;

  private String email;

  private int height;

  private int weight;

  private String image;

  public UserResponse(User user) {
    this.userId = user.getId();
    this.name = user.getName();
    this.nickName = user.getNickName();
    this.email = user.getEmail();
    this.height = user.getHeight();
    this.weight = user.getWeight();
    this.image = user.getImage();
  }
}
