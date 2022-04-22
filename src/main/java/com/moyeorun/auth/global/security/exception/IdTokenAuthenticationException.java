package com.moyeorun.auth.global.security.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.AuthenticationFailException;

public class IdTokenAuthenticationException extends AuthenticationFailException {

  public IdTokenAuthenticationException() {
    super(ErrorCode.IDTOKEN_AUTHENTICATION_FAIL);
  }
}
