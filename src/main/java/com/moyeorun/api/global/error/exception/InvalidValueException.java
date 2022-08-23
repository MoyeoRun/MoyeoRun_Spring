package com.moyeorun.api.global.error.exception;

import com.moyeorun.api.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {

  public InvalidValueException() {
    super(ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
