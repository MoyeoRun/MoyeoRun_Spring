package com.moyeorun.auth.domain.auth.dto.request;

import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.domain.contant.GenderType;
import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @NotBlank
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
