package com.moyeorun.auth.global.security.provider;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.global.config.property.OAuthProviderProperty;
import com.moyeorun.auth.global.security.authentication.GoogleAuthenticationIdToken;
import com.moyeorun.auth.global.security.exception.IdTokenAuthenticationException;
import com.moyeorun.auth.global.security.exception.InvalidIdTokenException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoogleIdTokenAuthenticationProvider implements AuthenticationProvider {

  private final String googleClientId;

  public GoogleIdTokenAuthenticationProvider(OAuthProviderProperty property) {
    this.googleClientId = property.getGoogle_client_id();
  }

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    GoogleAuthenticationIdToken token = (GoogleAuthenticationIdToken) authentication;
    Payload payload = verifierGoogleIdToken(token.getIdToken());

    if (payload == null) {
      log.error("idToken's payload is null");
      throw new IdTokenAuthenticationException();
    }

    SnsIdentify snsIdentify = new SnsIdentify(payload.getSubject(),
        token.getProviderType());
    String email = payload.getEmail();
    return new GoogleAuthenticationIdToken(email, snsIdentify, new ArrayList<>());
  }

  private Payload verifierGoogleIdToken(String idToken) {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
        new GsonFactory())
        .setAudience(Collections.singleton(googleClientId))
        .build();

    try {
      GoogleIdToken verifiedToken = verifier.verify(idToken);
      return verifiedToken.getPayload();
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage());
      throw new InvalidIdTokenException();
    } catch (IOException | GeneralSecurityException | NullPointerException e) {
      log.error(e.getMessage());
      throw new IdTokenAuthenticationException();
    }
  }


  @Override
  public boolean supports(Class<?> authentication) {
    return GoogleAuthenticationIdToken.class.isAssignableFrom(authentication);
  }
}
