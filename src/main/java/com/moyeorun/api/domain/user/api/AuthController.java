package com.moyeorun.api.domain.user.api;

import com.moyeorun.api.domain.user.application.AuthService;
import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.dto.request.RefreshRequest;
import com.moyeorun.api.domain.user.dto.request.SignInRequest;
import com.moyeorun.api.domain.user.dto.request.SignUpRequest;
import com.moyeorun.api.domain.user.dto.response.RefreshResponse;
import com.moyeorun.api.domain.user.dto.response.SignInResponse;
import com.moyeorun.api.domain.user.dto.response.SignUpResponse;
import com.moyeorun.api.global.common.AuthUser;
import com.moyeorun.api.global.common.LoginUser;
import com.moyeorun.api.global.common.response.MessageResponseDto;
import com.moyeorun.api.global.common.response.SuccessResponse;
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
  public ResponseEntity<?> logout(@LoginUser AuthUser user) {
    MessageResponseDto response = authService.logout(user.getUserId());

    return SuccessResponse.successWidthData(response);
  }

}
