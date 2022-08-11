package com.moyeorun.api.global.util;

import com.moyeorun.api.global.error.exception.InvalidValueException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeaderTokenExtractor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String HEADER_PREFIX = "Bearer ";

  public String extractToken(HttpServletRequest request) {
    String bearerHeader = request.getHeader(AUTHORIZATION_HEADER);

    if (StringUtils.hasText(bearerHeader) && bearerHeader.startsWith(HEADER_PREFIX)) {
      return bearerHeader.substring(HEADER_PREFIX.length());
    }
    throw new InvalidValueException();
  }

}
