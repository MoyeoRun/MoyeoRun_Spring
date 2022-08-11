package com.moyeorun.api.global.security.jwt;

import com.moyeorun.api.domain.user.domain.contant.RoleType;
import com.moyeorun.api.global.config.property.JwtProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String ISSUER = "moyeorun";
  private final Long ACCESS_TOKEN_EXPIRED_TIME_SECOND;
  private final Long REFRESH_TOKEN_EXPIRED_TIME_SECOND;
  private final Key key;
  private final Key refreshKey;

  public JwtProvider(JwtProperty jwtProperty) {
    this.ACCESS_TOKEN_EXPIRED_TIME_SECOND = jwtProperty.getAccess_token_expired_time();
    this.REFRESH_TOKEN_EXPIRED_TIME_SECOND = jwtProperty.getRefresh_token_expired_time();

    byte[] keyByte = Decoders.BASE64.decode(jwtProperty.getSecret_key());
    byte[] refreshKey = Decoders.BASE64.decode(jwtProperty.getRefresh_key());
    this.key = Keys.hmacShaKeyFor(keyByte);
    this.refreshKey = Keys.hmacShaKeyFor(refreshKey);
  }


  public String createAccessToken(String userId, RoleType role) {
    long now = (new Date()).getTime();
    Date accessTokenExpiredIn = new Date(now + ACCESS_TOKEN_EXPIRED_TIME_SECOND * 1000);
    return Jwts.builder()
        .setIssuer(ISSUER)
        .setSubject(userId)
        .setExpiration(accessTokenExpiredIn)
        .claim(AUTHORITIES_KEY, role)
        .signWith(key)
        .compact();
  }

  public String createRefreshToken() {
    long now = (new Date()).getTime();
    Date accessTokenExpiredIn = new Date(now + REFRESH_TOKEN_EXPIRED_TIME_SECOND * 1000);
    return Jwts.builder()
        .setIssuer(ISSUER)
        .setExpiration(accessTokenExpiredIn)
        .signWith(refreshKey)
        .compact();
  }

}
