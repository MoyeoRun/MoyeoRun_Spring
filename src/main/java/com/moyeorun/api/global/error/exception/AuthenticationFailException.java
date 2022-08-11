package com.moyeorun.api.global.error.exception;

import com.moyeorun.api.global.error.ErrorCode;

public class AuthenticationFailException extends BusinessException {

  public AuthenticationFailException() {
    super(ErrorCode.AUTHENTICATION_FAIL);
  }

  public AuthenticationFailException(ErrorCode errorCode) {
    super(errorCode);
  }
}
