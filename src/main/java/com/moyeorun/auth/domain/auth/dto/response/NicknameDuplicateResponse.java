package com.moyeorun.auth.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class NicknameDuplicateResponse {

  private Boolean isDuplicate;

  public NicknameDuplicateResponse(boolean isDuplicated) {
    this.isDuplicate = isDuplicated;
  }
}
