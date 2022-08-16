package com.moyeorun.api.global.security.jwt;

import com.moyeorun.api.domain.user.domain.contant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtClaimsVo {

  private String userId;

  private RoleType role;
}
