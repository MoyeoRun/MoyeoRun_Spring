package com.moyeorun.auth.domain.auth.api;

import com.moyeorun.auth.domain.auth.application.UserService;
import com.moyeorun.auth.domain.auth.dto.request.NicknameDuplicateRequest;
import com.moyeorun.auth.domain.auth.dto.response.NicknameDuplicateResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.UserResponse;
import com.moyeorun.auth.global.common.response.SuccessResponse;
import com.moyeorun.auth.global.security.authentication.IdTokenAuthentication;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/nickname/duplicate")
  public ResponseEntity<?> nicknameCheck(@RequestBody @Valid NicknameDuplicateRequest dto) {

    NicknameDuplicateResponse response = userService.nicknameDuplicate(dto);
    return SuccessResponse.successWidthData(response);
  }

  @GetMapping("")
  public ResponseEntity<?> getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Long userId = Long.valueOf(auth.getPrincipal().toString());
    
    UserResponse response = userService.getUser(userId);
    return SuccessResponse.successWidthData(response);
  }
}
