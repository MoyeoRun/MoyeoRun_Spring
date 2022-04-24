package com.moyeorun.auth.global.security.filter;

import com.moyeorun.auth.global.security.exception.InvalidJwtException;
import com.moyeorun.auth.global.security.jwt.JwtResolver;
import com.moyeorun.auth.global.util.HeaderTokenExtractor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtResolver jwtResolver;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final HeaderTokenExtractor headerTokenExtractor;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("jwt authentication filter");
    String jwtToken = headerTokenExtractor.extractToken(request);

    if (StringUtils.hasText(jwtToken) && jwtResolver.validationToken(jwtToken)) {
      Authentication authentication = jwtResolver.getAuthentication(jwtToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    List<String> skipPath = new ArrayList<>();
    skipPath.add("/");
    skipPath.add("/api/auth/**");
    skipPath.add("/api/user/nickname/duplicate");
    return skipPath.stream()
        .anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
  }
}
