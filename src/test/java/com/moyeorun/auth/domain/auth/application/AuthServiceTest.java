package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.domain.contant.GenderType;
import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.RefreshResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignUpResponse;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import com.moyeorun.auth.domain.auth.exception.NotSignInException;
import com.moyeorun.auth.global.common.response.MessageResponseDto;
import com.moyeorun.auth.global.config.property.JwtProperty;
import com.moyeorun.auth.global.error.ErrorCode;
import com.moyeorun.auth.global.error.exception.EntityNotFoundException;
import com.moyeorun.auth.global.error.exception.InvalidValueException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    String mockAccessToken = "accessToken";
    String mockRefreshToken = "refreshToken";

    Optional<User> user = stubUserOne();

    given(userRepository.existsUserByNickName(any())).willReturn(false);
    given(userRepository.existsUserBySnsIdentify(any())).willReturn(false);
    given(jwtProvider.createAccessToken(any())).willReturn(mockAccessToken);
    given(jwtProvider.createRefreshToken(any())).willReturn(mockRefreshToken);
    given(userRepository.save(any())).willReturn(user.get());

    SignUpResponse result = authService.signUp(dto, snsIdentify, email);

    assertEquals(result.getToken().getAccessToken(), mockAccessToken);
    assertEquals(result.getToken().getRefreshToken(), mockRefreshToken);
    assertEquals(result.getUserId(), user.get().getId());
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

  @DisplayName("refresh 테스트, 저장되지 않은 refreshToken 실패")
  @Test
  void refresh_실패() {
    String mockRefreshToken = "refreshToken";
    given(redisUtil.getValueByStringKey(any())).willReturn(null);

    NotSignInException exception = assertThrows(NotSignInException.class, () ->
        authService.refresh(mockRefreshToken));

    assertEquals(exception.getErrorCode(), ErrorCode.NOT_SIGN_IN_USER);
  }

  @DisplayName("refresh 테스트, 없는 유지로 실패")
  @Test
  void refresh_없는_유저_실패() {
    String mockRefreshToken = "refreshToken";
    String userIdString = "1";
    given(redisUtil.getValueByStringKey(any())).willReturn(userIdString);
    given(userRepository.findById(any())).willReturn(Optional.ofNullable(null));

    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
        authService.refresh(mockRefreshToken));

    assertEquals(exception.getErrorCode(), ErrorCode.ENTITY_NOT_FOUND);
  }

  @DisplayName("refresh 테스트 성공")
  @Test
  void refresh_성공() {
    String mockAccessToken = "accessToken";
    String mockRefreshToken = "refreshToken";
    String userIdString = "1";

    given(redisUtil.getValueByStringKey(any())).willReturn(userIdString);
    given(userRepository.findById(any())).willReturn(stubUserOne());
    given(jwtProvider.createAccessToken(any())).willReturn(mockAccessToken);

    RefreshResponse result = authService.refresh(mockRefreshToken);

    assertEquals(mockAccessToken, result.getAccessToken());
  }

  @DisplayName("로그아웃 테스트, 없는 유저로 실패")
  @Test
  void logOut_실패() {
    String mockRefreshToken = "refreshToken";

    given(redisUtil.getValueByStringKey(any())).willReturn(null);

    NotSignInException exception = assertThrows(NotSignInException.class,
        () -> authService.logout(mockRefreshToken));

    assertEquals(exception.getErrorCode(), ErrorCode.NOT_SIGN_IN_USER);
  }

  @DisplayName("로그아웃 성공")
  @Test
  void logOut_성공() {
    String mockRefreshToken = "refreshToken";

    given(redisUtil.getValueByStringKey(any())).willReturn("1");

    MessageResponseDto result = authService.logout(mockRefreshToken);

    assertEquals(result.getMessage(), "로그아웃 성공");
  }

  private SignUpRequest signUpRequestDtoMock() {
    return new SignUpRequest("idtokenValue", SnsProviderType.GOOGLE, "imageurl..", "name1",
        "nickname1", GenderType.MALE);
  }

  private Optional<User> stubUserOne() {
    User user = User.builder()
        .email("email@email.com")
        .snsIdentify(new SnsIdentify("12345", SnsProviderType.GOOGLE))
        .gender(GenderType.MALE)
        .nickName("nickname..")
        .name("name..")
        .image("imageurl..")
        .build();
    ReflectionTestUtils.setField(user, "id", 1L);
    return Optional.of(user);
  }
}
