package com.moyeorun.api.global.security.matcher;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class IdTokenFilterMatcher implements RequestMatcher {

  private final OrRequestMatcher orRequestMatcher;

  public IdTokenFilterMatcher(List<AntPathRequestMatcher> applyPath) {
    this.orRequestMatcher = new OrRequestMatcher(new ArrayList<>(applyPath));
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return orRequestMatcher.matches(request);
  }
}
