package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class NotAllowReservationRequestException extends BusinessException {

  public NotAllowReservationRequestException() {
    super(ErrorCode.NOT_ALLOW_RESERVATION_REQUEST);
  }
}
