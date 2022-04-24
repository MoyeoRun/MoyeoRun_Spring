package com.moyeorun.auth.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  //Common
  INTERNAL_SERVER_ERROR("000", "internal server error", 500),
  INVALID_INPUT_VALUE("001", "invalid input value", 400),
  ENTITY_NOT_FOUND("002", "entity not found", 404),
  AUTHENTICATION_FAIL("003", "authentication fail", 401),
  AUTHORIZATION_FAIL("010", "authorization fail", 403),

  //idToken
  INVALID_IDTOKEN("004", "invalid idToken", 400),
  IDTOKEN_AUTHENTICATION_FAIL("005", "idToken authentication fail", 401),

  //User
  NICKNAME_DUPLICATE("006", "duplicate nickname", 400),
  SNS_USER_DUPLICATE("007", "duplicate sns user", 400),
  USER_NOT_FOUND("011", "user not found", 404),
  //Jwt
  INVALID_JWT("008", "invalid jwt", 400),
  EXPIRED_JWT("009", "expired jwt", 401);


  private final String errorCase;
  private final String message;
  private final int statusCode;

  ErrorCode(String errorCase, String message, int statusCode) {
    this.errorCase = errorCase;
    this.message = message;
    this.statusCode = statusCode;
  }

}
