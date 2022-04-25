package com.moyeorun.auth.global.security.exception;


import org.springframework.security.core.AuthenticationException;

public class InvalidIdTokenException extends AuthenticationException {

  public InvalidIdTokenException() {
    super("인증 실패");
  }
}
