package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class RequireReservationException extends BusinessException {

  public RequireReservationException() {
    super(ErrorCode.REQUIRE_RESERVATION_ROOM);
  }
}
