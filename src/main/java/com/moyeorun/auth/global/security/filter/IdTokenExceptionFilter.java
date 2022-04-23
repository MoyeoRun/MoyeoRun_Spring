package com.moyeorun.auth.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.global.common.ReUsaAbleRequestWrapper;
import com.moyeorun.auth.global.common.response.ErrorResponseMap;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.BusinessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class IdTokenExceptionFilter extends OncePerRequestFilter {

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info(request.getRequestURI() + "idToken Exception Filter");

    ReUsaAbleRequestWrapper requestWrapper = new ReUsaAbleRequestWrapper(request);

    try {
      filterChain.doFilter(requestWrapper, response);
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

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    List<String> skipPath = new ArrayList<>();
    skipPath.add("/");
    skipPath.add("/oauth/**");
    return skipPath.stream()
        .anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
  }
}
