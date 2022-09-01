package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class LimitRoomUserCountException extends BusinessException {

  public LimitRoomUserCountException() {
    super(ErrorCode.LIMIT_ROOM_USER_COUNT);
  }
}
