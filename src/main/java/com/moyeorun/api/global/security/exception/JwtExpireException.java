package com.moyeorun.api.global.security.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.AuthenticationFailException;

public class JwtExpireException extends AuthenticationFailException {

  public JwtExpireException() {
    super(ErrorCode.EXPIRED_JWT);
  }
}
