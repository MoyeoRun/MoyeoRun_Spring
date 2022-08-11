package com.moyeorun.api.global.security.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.InvalidValueException;

public class InvalidJwtException extends InvalidValueException {

  public InvalidJwtException() {
    super(ErrorCode.INVALID_INPUT_VALUE);
  }
}
