package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class RequireJoinException extends BusinessException {

  public RequireJoinException() {
    super(ErrorCode.REQUIRE_JOIN_ROOM);
  }
}
