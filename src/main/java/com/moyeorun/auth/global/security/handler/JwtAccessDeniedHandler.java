package com.moyeorun.auth.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.global.common.response.ErrorResponseBody;
import com.moyeorun.auth.global.error.ErrorCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.error("권한 없음" + accessDeniedException.getMessage());

    sendResponse(response);
  }

  private void sendResponse(HttpServletResponse response) throws IOException {
    ErrorCode code = ErrorCode.AUTHORIZATION_FAIL;

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(code);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(code.getStatusCode());
    response.getWriter()
        .write(objectMapper.writeValueAsString(errorResponseBody));
  }
}
