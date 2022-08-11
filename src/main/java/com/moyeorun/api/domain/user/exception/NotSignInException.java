package com.moyeorun.api.domain.user.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.AuthenticationFailException;

public class NotSignInException extends AuthenticationFailException {

  public NotSignInException() {
    super(ErrorCode.NOT_SIGN_IN_USER);
  }
}
