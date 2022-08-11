package com.moyeorun.api.global.security.authentication;

import com.moyeorun.api.domain.user.domain.SnsIdentify;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class GoogleAuthenticationIdToken extends AbstractAuthenticationToken {

  private Object credentials;
  private String email;
  private SnsIdentify snsIdentify;

  public GoogleAuthenticationIdToken(String idToken) {
    super(new ArrayList<>());
    this.credentials = idToken;
    setAuthenticated(false);
  }

  public GoogleAuthenticationIdToken(String email, SnsIdentify snsIdentify,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.email = email;
    this.snsIdentify = snsIdentify;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getDetails() {
    return this.email;
  }

  @Override
  public Object getPrincipal() {
    return this.snsIdentify;
  }
}
