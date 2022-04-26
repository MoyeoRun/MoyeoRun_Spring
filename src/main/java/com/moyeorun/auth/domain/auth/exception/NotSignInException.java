package com.moyeorun.auth.domain.auth.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.AuthenticationFailException;

public class NotSignInException extends AuthenticationFailException {

  public NotSignInException() {
    super(ErrorCode.NOT_SIGN_IN_USER);
  }
}
