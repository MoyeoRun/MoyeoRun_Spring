package com.moyeorun.api.global.error.exception;

import com.moyeorun.api.global.error.ErrorCode;

public class AuthorizationFailException extends BusinessException{

  public AuthorizationFailException() {
    super(ErrorCode.AUTHORIZATION_FAIL);
  }
}
