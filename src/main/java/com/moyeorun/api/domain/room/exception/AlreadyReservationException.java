package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class AlreadyReservationException extends BusinessException {

  public AlreadyReservationException() {
    super(ErrorCode.ALREADY_RESERVATION_ROOM);
  }
}
