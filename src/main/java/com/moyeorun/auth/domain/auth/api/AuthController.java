package com.moyeorun.auth.domain.auth.api;

import com.moyeorun.auth.domain.auth.application.AuthService;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.dto.request.RefreshRequest;
import com.moyeorun.auth.domain.auth.dto.request.SignInRequest;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.RefreshResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignUpResponse;
import com.moyeorun.auth.global.common.response.MessageResponseDto;
import com.moyeorun.auth.global.common.response.SuccessResponse;
import com.moyeorun.auth.global.util.HeaderTokenExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

    SignUpResponse result = authService.signUp(signUpRequest, (SnsIdentify) auth.getPrincipal(),
        auth.getDetails().toString());

    return SuccessResponse.successWidthData(result);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    SignInResponse response = authService.signIn((SnsIdentify) auth.getPrincipal());

    return SuccessResponse.successWidthData(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestBody @Valid RefreshRequest refreshRequest) {

    RefreshResponse result = authService.refresh(refreshRequest);

    return SuccessResponse.successWidthData(result);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String userId = auth.getPrincipal().toString();

    MessageResponseDto response = authService.logout(userId);

    return SuccessResponse.successWidthData(response);
  }

}
