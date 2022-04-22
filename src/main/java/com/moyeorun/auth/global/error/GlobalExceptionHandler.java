package com.moyeorun.auth.global.error;

import com.moyeorun.auth.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<?> handleBusinessException(BusinessException e) {
    log.info(e.getMessage());

    return ResponseEntity.status(e.getErrorCode().getStatusCode())
        .body(e.getErrorCode().getMessage());
  }
}
