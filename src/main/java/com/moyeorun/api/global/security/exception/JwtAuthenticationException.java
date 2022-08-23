package com.moyeorun.api.global.security.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.AuthenticationFailException;

public class JwtAuthenticationException extends AuthenticationFailException {

  public JwtAuthenticationException() {
    super(ErrorCode.AUTHENTICATION_FAIL);
  }
}
