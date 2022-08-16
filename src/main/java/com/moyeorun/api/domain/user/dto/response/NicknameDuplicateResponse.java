package com.moyeorun.api.domain.user.dto.response;

import lombok.Getter;

@Getter
public class NicknameDuplicateResponse {

  private Boolean isDuplicate;

  public NicknameDuplicateResponse(boolean isDuplicated) {
    this.isDuplicate = isDuplicated;
  }
}
