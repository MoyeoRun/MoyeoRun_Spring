package com.moyeorun.auth.domain.auth.api;

import com.moyeorun.auth.domain.auth.application.AuthService;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.global.security.authentication.IdTokenAuthentication;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<?> singUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    IdTokenAuthentication tokenAuthentication = (IdTokenAuthentication) auth;

    User user = authService.signUp(signUpRequest, tokenAuthentication.getSnsIdentify(),
        tokenAuthentication.getEmail());
    
    return ResponseEntity.ok(user);
  }
}
