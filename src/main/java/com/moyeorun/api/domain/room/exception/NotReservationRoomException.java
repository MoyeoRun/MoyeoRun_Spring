package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class NotReservationRoomException extends BusinessException {

  public NotReservationRoomException() {
    super(ErrorCode.NOT_RESERVATION_ROOM);
  }
}
