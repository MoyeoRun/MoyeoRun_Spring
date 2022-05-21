package com.moyeorun.auth.global.security.jwt;

import com.moyeorun.auth.domain.auth.domain.contant.RoleType;
import com.moyeorun.auth.global.config.property.JwtProperty;
import com.moyeorun.auth.global.security.exception.InvalidJwtException;
import com.moyeorun.auth.global.security.exception.JwtAuthenticationException;
import com.moyeorun.auth.global.security.exception.JwtExpireException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtResolver {

  private static final String AUTHORITIES_KEY = "auth";
  private final Key key;

  public JwtResolver(JwtProperty jwtProperty) {

    byte[] keyByte = Decoders.BASE64.decode(jwtProperty.getSecret_key());
    this.key = Keys.hmacShaKeyFor(keyByte);
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    Collection<? extends GrantedAuthority> authorities = new ArrayList<>(
        List.of(new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString())));

    return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
  }

  public boolean validationToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("잘못된 JWT 서명");
      throw new JwtAuthenticationException();
    } catch (ExpiredJwtException e) {
      log.error("만료된 JWT 토큰");
      throw new JwtExpireException();
    } catch (UnsupportedJwtException e) {
      log.error("지원하지 않는 JWT 토큰");
      throw new JwtAuthenticationException();
    } catch (IllegalArgumentException e) {
      log.error("JWT 토큰이 잘못됨");
      throw new InvalidJwtException();
    }
  }

  public JwtClaimsVo getClaimByJwt(String accessToken) {
    try {
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
          .getBody();
      return new JwtClaimsVo(claims.getSubject(),
          RoleType.valueOf(claims.get(AUTHORITIES_KEY).toString()));
    } catch (ExpiredJwtException e) {
      return new JwtClaimsVo(e.getClaims().getSubject(),
          RoleType.valueOf(e.getClaims().get(AUTHORITIES_KEY).toString()));
    } catch (Exception e) {
      throw new InvalidJwtException();
    }
  }
}
