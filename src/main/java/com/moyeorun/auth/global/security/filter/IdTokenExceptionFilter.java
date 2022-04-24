package com.moyeorun.auth.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.global.common.ReUsaAbleRequestWrapper;
import com.moyeorun.auth.global.common.response.ErrorResponseBody;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.BusinessException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class IdTokenExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info(request.getRequestURI() + "idToken Exception Filter");

    ReUsaAbleRequestWrapper requestWrapper = new ReUsaAbleRequestWrapper(request);

    try {
      filterChain.doFilter(requestWrapper, response);
    } catch (BusinessException e) {
      e.printStackTrace();
      responseHandle(response, e.getErrorCode());
    } catch (RuntimeException e) {
      e.printStackTrace();
      responseHandle(response, ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private void responseHandle(HttpServletResponse response, ErrorCode errorCode)
      throws IOException {

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(errorCode.getStatusCode());
    response.getWriter()
        .write(objectMapper.writeValueAsString(errorResponseBody));
  }
}
