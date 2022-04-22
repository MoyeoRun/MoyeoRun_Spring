package com.moyeorun.auth.global.security.authentication;

import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class GoogleAuthenticationIdToken extends AbstractAuthenticationToken implements
    IdTokenAuthentication {

  private String idToken;
  private String email;
  private SnsIdentify snsIdentify;
  private final SnsProviderType providerType = SnsProviderType.GOOGLE;

  public GoogleAuthenticationIdToken(String idToken) {
    super(null);
    this.idToken = idToken;
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
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }
}
