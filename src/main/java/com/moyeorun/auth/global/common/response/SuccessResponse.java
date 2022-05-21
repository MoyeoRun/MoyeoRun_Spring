package com.moyeorun.auth.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponse {

  private Object data;

  private SuccessResponse(Object data) {
    this.data = data;
  }


  public static ResponseEntity<?> successWidthData(Object data) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new SuccessResponse(data));
  }
}
