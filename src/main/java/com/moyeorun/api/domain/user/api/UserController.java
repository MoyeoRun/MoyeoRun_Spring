package com.moyeorun.api.domain.user.api;

import com.moyeorun.api.domain.user.application.UserService;
import com.moyeorun.api.domain.user.dto.request.NicknameDuplicateRequest;
import com.moyeorun.api.domain.user.dto.response.NicknameDuplicateResponse;
import com.moyeorun.api.domain.user.dto.response.UserResponse;
import com.moyeorun.api.global.common.AuthUser;
import com.moyeorun.api.global.common.LoginUser;
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
  public ResponseEntity<?> getUser(@LoginUser AuthUser user) {
    UserResponse response = userService.getUser(user.getUserId());
    return SuccessResponse.successWidthData(response);
  }
}
