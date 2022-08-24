package com.moyeorun.api.domain.room.dto.response;


import com.moyeorun.api.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomUserResponse {

  private Long userId;
  private String nickname;
  private String profileImage;

  @Builder
  private RoomUserResponse(Long userId, String nickname, String profileImage) {
    this.userId = userId;
    this.nickname = nickname;
    this.profileImage = profileImage;
  }

  public static RoomUserResponse from(User user){
    return RoomUserResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickName())
        .profileImage(user.getImage())
        .build();
  }
}
