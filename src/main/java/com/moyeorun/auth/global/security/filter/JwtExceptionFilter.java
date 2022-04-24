package com.moyeorun.auth.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.global.common.response.ErrorResponseMap;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.BusinessException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("jwt Exception filter");
    try {
      filterChain.doFilter(request, response);
    } catch (BusinessException e) {
      e.printStackTrace();
      responseHandle(response, e);
    }
  }

  private void responseHandle(HttpServletResponse response, BusinessException e)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ErrorCode errorCode = e.getErrorCode();
    ErrorResponseMap errorResponseMap = new ErrorResponseMap(errorCode);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(errorCode.getStatusCode());
    response.getWriter()
        .write(objectMapper.writeValueAsString(errorResponseMap.getMap()));
  }

}
