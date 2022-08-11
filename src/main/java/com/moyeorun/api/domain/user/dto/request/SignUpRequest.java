package com.moyeorun.api.domain.user.dto.request;

import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.domain.user.domain.contant.GenderType;
import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  private String idToken;

  private SnsProviderType providerType;

  @NotBlank
  private String image;

  @NotBlank
  private String name;

  @NotBlank
  private String nickName;

  @NotNull
  private GenderType gender;

  public User toEntity(SnsIdentify snsIdentify, String email) {
    return User.builder()
        .image(image)
        .name(name)
        .nickName(nickName)
        .gender(gender)
        .snsIdentify(snsIdentify)
        .email(email)
        .build();
  }
}
