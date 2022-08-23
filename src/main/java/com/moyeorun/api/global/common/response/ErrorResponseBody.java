package com.moyeorun.api.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.moyeorun.api.global.error.ErrorCode;
import lombok.Getter;

@Getter
@JsonPropertyOrder(value = {"message", "case"})
public class ErrorResponseBody {

  @JsonProperty("case")
  private int errorCase;

  @JsonProperty("message")
  @JsonInclude(Include.NON_EMPTY)
  private String message;

  public ErrorResponseBody(ErrorCode errorCode) {
    this.errorCase = errorCode.getErrorCase();
  }

  public ErrorResponseBody(ErrorCode errorCode, String message) {
    this.errorCase = errorCode.getErrorCase();
    this.message = message;
  }
}
