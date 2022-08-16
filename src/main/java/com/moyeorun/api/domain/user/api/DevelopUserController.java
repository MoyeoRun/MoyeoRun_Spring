package com.moyeorun.api.domain.user.api;

import com.moyeorun.api.domain.user.application.AuthService;
import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import com.moyeorun.api.domain.user.dto.request.DevelopUserSignInRequest;
import com.moyeorun.api.domain.user.dto.response.SignInResponse;
import com.moyeorun.api.global.common.response.SuccessResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dev")
public class DevelopUserController {

  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<?> developSignIn(@Valid @RequestBody DevelopUserSignInRequest dto){
    SignInResponse signInResponse = authService.signIn(
        new SnsIdentify(dto.getId(), SnsProviderType.GOOGLE));
    return SuccessResponse.successWidthData(signInResponse);
  }
}
