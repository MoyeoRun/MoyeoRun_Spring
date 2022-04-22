package com.moyeorun.auth.domain.auth.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.InvalidValueException;

public class DuplicateSnsUser extends InvalidValueException {

  public DuplicateSnsUser() {
    super(ErrorCode.SNS_USER_DUPLICATE);
  }
}
