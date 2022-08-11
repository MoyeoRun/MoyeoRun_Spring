package com.moyeorun.api.global.common;

import com.moyeorun.api.domain.user.domain.contant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {

  private Long userId;
  private RoleType role;
}
