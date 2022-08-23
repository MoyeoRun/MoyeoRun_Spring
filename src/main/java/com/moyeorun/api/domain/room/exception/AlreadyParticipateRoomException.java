package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class AlreadyParticipateRoomException extends BusinessException {

  public AlreadyParticipateRoomException() {
    super(ErrorCode.ALREADY_PARTICIPATE_ROOM);
  }
}
