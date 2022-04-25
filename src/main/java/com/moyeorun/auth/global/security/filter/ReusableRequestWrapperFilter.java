package com.moyeorun.auth.global.security.filter;


import com.moyeorun.auth.global.common.ReusableRequestWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ReusableRequestWrapperFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info(request.getRequestURI() + "ReusableRequest Wrapper");

    ReusableRequestWrapper requestWrapper = new ReusableRequestWrapper(request);

    filterChain.doFilter(requestWrapper, response);

  }

}
