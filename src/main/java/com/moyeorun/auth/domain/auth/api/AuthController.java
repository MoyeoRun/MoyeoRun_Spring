package com.moyeorun.auth.domain.auth.api;

import com.moyeorun.auth.domain.auth.application.AuthService;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.SignInRequest;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignUpResponse;
import com.moyeorun.auth.domain.auth.dto.response.TokenDto;
import com.moyeorun.auth.global.common.response.MessageResponseDto;
import com.moyeorun.auth.global.common.response.SuccessResponse;
import com.moyeorun.auth.global.security.authentication.IdTokenAuthentication;
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
    IdTokenAuthentication tokenAuthentication = (IdTokenAuthentication) auth;

    SignUpResponse result = authService.signUp(signUpRequest, tokenAuthentication.getSnsIdentify(),
        tokenAuthentication.getEmail());

    return SuccessResponse.successWidthData(result);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    IdTokenAuthentication tokenAuthentication = (IdTokenAuthentication) auth;

    SignInResponse response = authService.signIn(tokenAuthentication.getSnsIdentify());

    return SuccessResponse.successWidthData(response);
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request) {
    String refreshToken = headerTokenExtractor.extractToken(request);

    TokenDto tokenDto = authService.refresh(refreshToken);

    return SuccessResponse.successWidthData(tokenDto);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String refreshToken = headerTokenExtractor.extractToken(request);

    MessageResponseDto response = authService.logout(refreshToken);

    return SuccessResponse.successWidthData(response);
  }

}
