package com.moyeorun.api.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  //Common
  INVALID_INPUT_VALUE(100, "invalid input value", 400),
  AUTHENTICATION_FAIL(101, "authentication fail", 401),
  AUTHORIZATION_FAIL(102, "authorization fail", 403),
  ENTITY_NOT_FOUND(103, "entity not found", 404),
  EXPIRED_JWT(104, "expired jwt", 401),
  NOT_SIGN_IN_USER(105, "not sign in user request", 401),
  

  NOT_SUPPORT_METHOD(198, "not support method", 400),
  INTERNAL_SERVER_ERROR(199, "internal server error", 500),
  //idToken
//  INVALID_IDTOKEN(110, "invalid idToken", 400),
//  IDTOKEN_AUTHENTICATION_FAIL("005", "idToken authentication fail", 401),

  //User  11X
  NICKNAME_DUPLICATE(110, "duplicate nickname", 400),
  SNS_USER_DUPLICATE(111, "duplicate sns user", 400);
//  USER_NOT_FOUND(112, "user not found", 404);

  //Jwt 104
//  INVALID_JWT(113, "invalid jwt", 400),


  private final int errorCase;
  private final String message;
  private final int statusCode;

  ErrorCode(int errorCase, String message, int statusCode) {
    this.errorCase = errorCase;
    this.message = message;
    this.statusCode = statusCode;
  }

}
