package com.moyeorun.api.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  //Common
  INVALID_INPUT_VALUE(100, "요청 값이 잘못됐습니다.", 400),
  AUTHENTICATION_FAIL(101, "인증이 실패했습니다.", 401),
  AUTHORIZATION_FAIL(102, "권한이 없습니다.", 403),
  ENTITY_NOT_FOUND(103, "해당 자원을 찾을 수 없습니다.", 404),
  EXPIRED_JWT(104, "토큰 기간 만료", 401),
  NOT_SIGN_IN_USER(105, "로그인이 필요합니다.", 401),

  NICKNAME_DUPLICATE(110, "닉네임 중복됐습니다.", 400),
  SNS_USER_DUPLICATE(111, "이미 가입된 유저입니다.", 400),

  ALREADY_PARTICIPATE_ROOM(120, "이미 참여된 방입니다.", 400),
  NOT_ALLOW_RESERVATION_REQUEST(121, "방 예약과 관련된 시간이 아닙니다.", 400),
  ALREADY_RESERVATION_ROOM(122, "이미 예약한 방입니다.", 400),
  REQUIRE_RESERVATION_ROOM(123, "예약된 유저가 아닙니다,", 400),
  NOT_ALLOW_HOST_USER_REQUEST(124, "방장은 해당 요청을 보낼 수 없습니다.", 400),
  NOT_ALLOW_JOIN_REQUEST(125, "방 참여와 관련된 시간이 아닙니다.", 400),
  ALREADY_JOIN_ROOM(126, "이미 참여한 방입니다.", 400),
  REQUIRE_JOIN_ROOM(127, "참여한 유저가 아닙니다.", 400),
  LIMIT_ROOM_USER_COUNT(128, "방이 최대 인원입니다.", 400),
  NOT_DELETE_ROOM_TIME(129, "방 삭제를 할 수 없는 시간입니다.", 400),
  NOT_SUPPORT_METHOD(198, "지원히지 않는 메소드입니다.", 400),
  INTERNAL_SERVER_ERROR(199, "서버 에러", 500);

  private final int errorCase;
  private final String message;
  private final int statusCode;

  ErrorCode(int errorCase, String message, int statusCode) {
    this.errorCase = errorCase;
    this.message = message;
    this.statusCode = statusCode;
  }

}
