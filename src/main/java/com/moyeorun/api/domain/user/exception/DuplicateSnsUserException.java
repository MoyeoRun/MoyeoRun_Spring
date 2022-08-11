package com.moyeorun.api.domain.user.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.InvalidValueException;

public class DuplicateSnsUserException extends InvalidValueException {

  public DuplicateSnsUserException() {
    super(ErrorCode.SNS_USER_DUPLICATE);
  }
}
