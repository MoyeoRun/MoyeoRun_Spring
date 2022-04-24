package com.moyeorun.auth.domain.auth.exception;

import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}
