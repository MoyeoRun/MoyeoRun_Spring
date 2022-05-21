package com.moyeorun.auth.global.error.exception;

import com.moyeorun.auth.global.error.ErrorCode;

public class AuthenticationFailException extends BusinessException {

  public AuthenticationFailException() {
    super(ErrorCode.AUTHENTICATION_FAIL);
  }

  public AuthenticationFailException(ErrorCode errorCode) {
    super(errorCode);
  }
}
