package com.moyeorun.auth.domain.auth.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameDuplicateRequest {

  @NotBlank
  private String nickName;

}
