package com.moyeorun.api.domain.user.application;

import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.domain.user.domain.contant.GenderType;
import com.moyeorun.api.domain.user.domain.contant.RoleType;
import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import com.moyeorun.api.domain.user.dto.request.RefreshRequest;
import com.moyeorun.api.domain.user.dto.request.SignUpRequest;
import com.moyeorun.api.domain.user.dto.response.RefreshResponse;
import com.moyeorun.api.domain.user.dto.response.SignInResponse;
import com.moyeorun.api.domain.user.dto.response.SignUpResponse;
import com.moyeorun.api.domain.user.exception.DuplicateNicknameException;
import com.moyeorun.api.domain.user.exception.DuplicateSnsUserException;
import com.moyeorun.api.domain.user.exception.NotSignInException;
import com.moyeorun.api.global.common.response.MessageResponseDto;
import com.moyeorun.api.global.config.property.JwtProperty;
import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.security.exception.InvalidJwtException;
import com.moyeorun.api.global.security.jwt.JwtClaimsVo;
import com.moyeorun.api.global.security.jwt.JwtProvider;
import com.moyeorun.api.global.security.jwt.JwtResolver;
import com.moyeorun.api.global.util.RedisUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  JwtProvider jwtProvider;

  @Mock
  JwtResolver jwtResolver;

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
    Optional<User> user = stubUserOne();

    given(userRepository.existsUserByNickName(any())).willReturn(false);
    given(userRepository.existsUserBySnsIdentify(any())).willReturn(false);
    given(userRepository.save(any())).willReturn(user.get());

    SignUpResponse result = authService.signUp(dto, snsIdentify, email);

    assertEquals(user.get().getId(), result.getUserId());
  }

  @DisplayName("로그인 시 없는 유저 로그인 실패")
  @Test
  void singIn_실패후_isNewUserFalse() {
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);

    given(userRepository.findBySnsIdentify(any())).willReturn(Optional.ofNullable(null));

    SignInResponse result = authService.signIn(snsIdentify);

    assertTrue(result.getIsNewUser());
  }

  @DisplayName("로그인 성공")
  @Test
  void singIn_성공() {
    SnsIdentify snsIdentify = new SnsIdentify("12345", SnsProviderType.GOOGLE);

    given(userRepository.findBySnsIdentify(any())).willReturn(stubUserOne());

    SignInResponse result = authService.signIn(snsIdentify);

    assertFalse(result.getIsNewUser());
    assertEquals(1L, result.getUserId());
  }

  @DisplayName("refresh 테스트, 저장되지 않은 refreshToken 실패(로그인 안됨)")
  @Test
  void refresh_로그인_안된유저_실패() {
    String mockAccessToken = "accessToken";
    String mockRefreshToken = "refreshToken";
    RefreshRequest refreshRequest = new RefreshRequest(mockAccessToken, mockRefreshToken);
    JwtClaimsVo jwtClaimsVo = new JwtClaimsVo("1", RoleType.USER);

    given(jwtResolver.getClaimByJwt(any())).willReturn(jwtClaimsVo);
    given(redisUtil.getValueByStringKey(any())).willReturn(null);

    NotSignInException exception = assertThrows(NotSignInException.class, () ->
        authService.refresh(refreshRequest));

    assertEquals(ErrorCode.NOT_SIGN_IN_USER, exception.getErrorCode());
  }

  @DisplayName("refresh 테스트,저장된 토큰과 불일치로 실패")
  @Test
  void refresh_저장된토큰과_불일치() {
    String mockRefreshToken = "refreshToken";
    String userIdString = "1";
    String mockAccessToken = "accessToken";
    RefreshRequest refreshRequest = new RefreshRequest(mockAccessToken, mockRefreshToken);
    JwtClaimsVo jwtClaimsVo = new JwtClaimsVo(userIdString, RoleType.USER);

    given(jwtResolver.getClaimByJwt(any())).willReturn(jwtClaimsVo);
    given(redisUtil.getValueByStringKey(any())).willReturn("otherRefreshToken");

    InvalidJwtException exception = assertThrows(InvalidJwtException.class, () ->
        authService.refresh(refreshRequest));

    assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCode());
  }

  @DisplayName("refresh 테스트 성공")
  @Test
  void refresh_성공() {
    String mockAccessToken = "accessToken";
    String mockRefreshToken = "refreshToken";
    String createdAccessToken = "createdAccessToken";
    String userIdString = "1";
    RefreshRequest refreshRequest = new RefreshRequest(mockAccessToken, mockRefreshToken);
    JwtClaimsVo jwtClaimsVo = new JwtClaimsVo(userIdString, RoleType.USER);

    given(jwtResolver.getClaimByJwt(any())).willReturn(jwtClaimsVo);
    given(redisUtil.getValueByStringKey(any())).willReturn(mockRefreshToken);
    given(jwtProvider.createAccessToken(any(), any())).willReturn(createdAccessToken);

    RefreshResponse result = authService.refresh(refreshRequest);

    assertEquals(createdAccessToken, result.getAccessToken());
  }

  @DisplayName("로그아웃 테스트, 로그인이 안된 유저 실패")
  @Test
  void logOut_실패() {

    given(redisUtil.getValueByStringKey(any())).willReturn(null);

    NotSignInException exception = assertThrows(NotSignInException.class,
        () -> authService.logout("1"));

    assertEquals(ErrorCode.NOT_SIGN_IN_USER, exception.getErrorCode());
  }

  @DisplayName("로그아웃 성공")
  @Test
  void logOut_성공() {
    String mockRefreshToken = "refreshToken";

    given(redisUtil.getValueByStringKey(any())).willReturn(mockRefreshToken);

    MessageResponseDto result = authService.logout("1");

    assertEquals("로그아웃 성공", result.getMessage());
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
