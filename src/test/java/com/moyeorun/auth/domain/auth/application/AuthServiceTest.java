package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import com.moyeorun.auth.global.common.response.MessageResponseDto;
import com.moyeorun.auth.global.config.property.JwtProperty;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.EntityNotFoundException;
import com.moyeorun.auth.global.security.jwt.JwtProvider;
import com.moyeorun.auth.global.util.RedisUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  JwtProvider jwtProvider;

  @Mock
  JwtProperty jwtProperty;

  @Mock
  RedisUtil redisUtil;

  @InjectMocks
  AuthService authService;
//
//  @BeforeEach
//  void setUp() {
//    JwtProperty jwtProperty = new JwtProperty();
//    jwtProperty.setAccess_token_expired_time();
//    this.jwtProperty =
//  }


  @DisplayName("회원가입시 닉네임 중복 실패")
  @Test
  void signUp_닉네임중복테스트() {
    SignUpRequest dto = signUpRequestDtoMock();
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);
    String email = "test@test.com";

    given(userRepository.existsUserByNickName(any())).willReturn(true);
    DuplicateNicknameException exception = assertThrows(DuplicateNicknameException.class,
        () -> authService.signUp(dto, snsIdentify, email));

    assertEquals(ErrorCode.NICKNAME_DUPLICATE, exception.getErrorCode());
  }

  @DisplayName("회원가입시 유저 중복 실패")
  @Test
  void signUp_유저중복테스트() {
    SignUpRequest dto = signUpRequestDtoMock();
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);
    String email = "test@test.com";

    given(userRepository.existsUserByNickName(any())).willReturn(false);
    given(userRepository.existsUserBySnsIdentify(any())).willReturn(true);

    DuplicateSnsUserException exception = assertThrows(DuplicateSnsUserException.class,
        () -> authService.signUp(dto, snsIdentify, email));

    assertEquals(ErrorCode.SNS_USER_DUPLICATE, exception.getErrorCode());
  }

  @DisplayName("회원가입 성공")
  @Test
  void signUp_성공() {
    SignUpRequest dto = signUpRequestDtoMock();
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);
    String email = "test@test.com";

    given(userRepository.existsUserByNickName(any())).willReturn(false);
    given(userRepository.existsUserBySnsIdentify(any())).willReturn(false);

    MessageResponseDto result = authService.signUp(dto, snsIdentify, email);

    assertEquals(result.getMessage(), "회원가입 성공");
  }

  @DisplayName("로그인 시 없는 유저 로그인 실패")
  @Test
  void singIn_실패후_isNewUserFalse() {
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);

    given(userRepository.findBySnsIdentify(any())).willReturn(Optional.ofNullable(null));

    SignInResponse result = authService.signIn(snsIdentify);

    assertEquals(result.isNewUser(), true);
  }

  @DisplayName("로그인 성공")
  @Test
  void singIn_성공() {
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);
    String mockAccessToken = "accessToken";
    String mockRefreshToken = "refreshToken";

    given(userRepository.findBySnsIdentify(any())).willReturn(stubUserOne());
    given(jwtProvider.createAccessToken(any())).willReturn(mockAccessToken);
    given(jwtProvider.createRefreshToken(any())).willReturn(mockRefreshToken);

    SignInResponse result = authService.signIn(snsIdentify);

    assertEquals(result.isNewUser(), false);
    assertEquals(result.getUserId(), 1L);
    assertEquals(result.getToken().getAccessToken(), mockAccessToken);
    assertEquals(result.getToken().getRefreshToken(), mockRefreshToken);
  }



  private SignUpRequest signUpRequestDtoMock() {
    return new SignUpRequest("idtokenValue", SnsProviderType.GOOGLE, "imageurl..", "name1",
        "nickname1", 100, 100);
  }

  private Optional<User> stubUserOne() {
    User user = User.builder()
        .email("email@email.com")
        .snsIdentify(new SnsIdentify("12345", SnsProviderType.GOOGLE))
        .height(100)
        .weight(100)
        .nickName("nickname..")
        .name("name..")
        .image("imageurl..")
        .build();
    ReflectionTestUtils.setField(user, "id", 1L);
    return Optional.of(user);
  }
}
