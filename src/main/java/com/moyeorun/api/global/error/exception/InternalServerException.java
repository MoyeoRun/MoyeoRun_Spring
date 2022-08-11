package com.moyeorun.api.global.error.exception;

import com.moyeorun.api.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
  //filter handling 용

  public InternalServerException() {
    super(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
