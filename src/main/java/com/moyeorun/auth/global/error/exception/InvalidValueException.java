package com.moyeorun.auth.global.error.exception;

import com.moyeorun.auth.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {

  public InvalidValueException() {
    super(ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
