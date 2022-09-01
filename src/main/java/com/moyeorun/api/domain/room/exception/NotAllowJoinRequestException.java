package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class NotAllowJoinRequestException extends BusinessException {

  public NotAllowJoinRequestException() {
    super(ErrorCode.NOT_ALLOW_JOIN_REQUEST);
  }
}
