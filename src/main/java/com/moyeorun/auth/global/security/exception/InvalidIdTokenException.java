package com.moyeorun.auth.global.security.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.InvalidValueException;

public class InvalidIdTokenException extends InvalidValueException {

  public InvalidIdTokenException() {
    super(ErrorCode.INVALID_IDTOKEN);
  }
}
