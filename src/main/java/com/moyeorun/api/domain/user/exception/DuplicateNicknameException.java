package com.moyeorun.api.domain.user.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.InvalidValueException;

public class DuplicateNicknameException extends InvalidValueException {

  public DuplicateNicknameException() {
    super(ErrorCode.NICKNAME_DUPLICATE);
  }
}
