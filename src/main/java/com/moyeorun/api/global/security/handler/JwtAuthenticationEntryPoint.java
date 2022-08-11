package com.moyeorun.api.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.api.global.common.response.ErrorResponseBody;
import com.moyeorun.api.global.error.ErrorCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    log.error("인증 실패", authException.getMessage());
    sendResponse(response);
  }

  private void sendResponse(HttpServletResponse response) throws IOException {
    ErrorCode code = ErrorCode.AUTHENTICATION_FAIL;

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(code);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(code.getStatusCode());
    response.getWriter()
        .write(objectMapper.writeValueAsString(errorResponseBody));
  }
}
