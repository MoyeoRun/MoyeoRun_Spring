package com.moyeorun.auth.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.global.common.response.ErrorResponseMap;
import com.moyeorun.auth.global.error.exception.BusinessException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<?> handleBusinessException(BusinessException e) throws IOException {
    log.info(e.getMessage());

    ErrorResponseMap errorResponseMap = new ErrorResponseMap(e.getErrorCode());

    return ResponseEntity.status(e.getErrorCode().getStatusCode())
        .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
        .body(objectMapper.writeValueAsString(errorResponseMap.getMap()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  private ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) throws IOException {
    log.error(e.getMessage());

    ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;
    ErrorResponseMap errorResponseMap = new ErrorResponseMap(code);

    return ResponseEntity.status(code.getStatusCode())
        .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
        .body(objectMapper.writeValueAsString(errorResponseMap.getMap()));
  }
}
