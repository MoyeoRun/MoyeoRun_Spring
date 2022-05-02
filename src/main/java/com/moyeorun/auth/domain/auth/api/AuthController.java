package com.moyeorun.auth.domain.auth.api;

import com.moyeorun.auth.domain.auth.application.AuthService;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
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
  private final HeaderTokenExtractor headerTokenExtractor;

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

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request) {
    String refreshToken = headerTokenExtractor.extractToken(request);

    RefreshResponse result = authService.refresh(refreshToken);

    return SuccessResponse.successWidthData(result);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String refreshToken = headerTokenExtractor.extractToken(request);

    MessageResponseDto response = authService.logout(refreshToken);

    return SuccessResponse.successWidthData(response);
  }

}
