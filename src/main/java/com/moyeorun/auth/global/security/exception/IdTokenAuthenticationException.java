package com.moyeorun.auth.global.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IdTokenAuthenticationException extends AuthenticationException {

  public IdTokenAuthenticationException() {
    super("인증 실패");
  }
}
