package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class NotDeleteRoomTimeException extends BusinessException {

  public NotDeleteRoomTimeException() {
    super(ErrorCode.NOT_DELETE_ROOM_TIME);
  }
}
