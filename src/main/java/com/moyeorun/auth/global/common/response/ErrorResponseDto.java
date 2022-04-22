package com.moyeorun.auth.global.common.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

  private final Map<String, String> map = new HashMap<>();

  public ErrorResponseDto(String errorCode) {
    map.put("case", errorCode);
  }

  public ErrorResponseDto(String message, String errorCode) {
    map.put("message", message);
    map.put("case", errorCode);
  }

}
