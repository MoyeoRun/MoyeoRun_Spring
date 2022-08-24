package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class AlreadyJoinRoomException extends BusinessException {

  public AlreadyJoinRoomException() {
    super(ErrorCode.ALREADY_JOIN_ROOM);
  }
}
