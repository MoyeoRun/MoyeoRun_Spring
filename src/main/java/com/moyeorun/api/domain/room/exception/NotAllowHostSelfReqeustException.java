package com.moyeorun.api.domain.room.exception;

import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;

public class NotAllowHostSelfReqeustException extends BusinessException {

  public NotAllowHostSelfReqeustException() {
    super(ErrorCode.NOT_ALLOW_HOST_USER_REQUEST);
  }
}
