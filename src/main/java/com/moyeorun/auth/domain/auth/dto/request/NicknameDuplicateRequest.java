package com.moyeorun.auth.domain.auth.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class NicknameDuplicateRequest {

  @NotBlank
  private String nickName;

}
