package com.moyeorun.auth.global.common.response;

import com.moyeorun.auth.global.error.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponseMap {

  private final Map<String, String> map = new HashMap<>();

  public ErrorResponseMap(ErrorCode errorCode) {
    this.map.put("case", errorCode.getErrorCase());
  }

  @Builder
  public ErrorResponseMap(String message, ErrorCode errorCode) {
    this.map.put("message", message);
    this.map.put("case", errorCode.getErrorCase());
  }

}
