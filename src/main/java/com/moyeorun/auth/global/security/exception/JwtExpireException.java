package com.moyeorun.auth.global.security.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.AuthenticationFailException;

public class JwtExpireException extends AuthenticationFailException {

  public JwtExpireException() {
    super(ErrorCode.EXPIRED_JWT);
  }
}
