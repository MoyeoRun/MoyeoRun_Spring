package com.moyeorun.auth.global.security.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.InvalidValueException;

public class InvalidJwtException extends InvalidValueException {

  public InvalidJwtException() {
    super(ErrorCode.INVALID_INPUT_VALUE);
  }
}
