package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.dto.request.NicknameDuplicateRequest;
import com.moyeorun.auth.domain.auth.dto.response.NicknameDuplicateResponse;
import com.moyeorun.auth.domain.auth.dto.response.UserResponse;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.EntityNotFoundException;
import com.moyeorun.auth.setup.domain.UserMockBuilder;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @DisplayName("닉네임 중복")
  @Test
  void nickname_중복() {
    NicknameDuplicateRequest request = new NicknameDuplicateRequest("nickname");

    given(userRepository.existsUserByNickName(any())).willReturn(true);

    NicknameDuplicateResponse result = userService.nicknameDuplicate(request);
    assertTrue(result.getIsDuplicate());
  }

  @DisplayName("닉네임 중복X")
  @Test
  void nickname_중복X() {
    NicknameDuplicateRequest request = new NicknameDuplicateRequest("nickname");

    given(userRepository.existsUserByNickName(any())).willReturn(false);

    NicknameDuplicateResponse result = userService.nicknameDuplicate(request);
    assertFalse(result.getIsDuplicate());
  }

  @DisplayName("유저 조회 실패_없는 유저")
  @Test
  void getUser_실패(){
    Long id = 1L;

    given(userRepository.findById(any())).willReturn(Optional.empty());

    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()-> userService.getUser(id));

    assertEquals(ErrorCode.ENTITY_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("유저 조회 성공")
  @Test
  void getUser_성공(){
    Long id = 1L;
    given(userRepository.findById(any())).willReturn(UserMockBuilder.ofOptional());

    UserResponse result = userService.getUser(id);
    assertEquals(id, result.getUserId());
  }
}
