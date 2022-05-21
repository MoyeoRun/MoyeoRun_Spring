package com.moyeorun.auth.domain.auth.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.InvalidValueException;

public class DuplicateNicknameException extends InvalidValueException {

  public DuplicateNicknameException() {
    super(ErrorCode.NICKNAME_DUPLICATE);
  }
}
