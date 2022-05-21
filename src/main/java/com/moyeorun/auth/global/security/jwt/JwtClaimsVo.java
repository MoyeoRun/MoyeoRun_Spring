package com.moyeorun.auth.global.security.jwt;

import com.moyeorun.auth.domain.auth.domain.contant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtClaimsVo {

  private String userId;

  private RoleType role;
}
