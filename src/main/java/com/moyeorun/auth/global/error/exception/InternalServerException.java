package com.moyeorun.auth.global.error.exception;

import com.moyeorun.auth.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
  //filter handling 용

  public InternalServerException() {
    super(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
