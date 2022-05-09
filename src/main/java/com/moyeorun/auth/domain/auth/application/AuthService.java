package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.RefreshRequest;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.RefreshResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignUpResponse;
import com.moyeorun.auth.domain.auth.dto.response.TokenDto;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import com.moyeorun.auth.domain.auth.exception.NotSignInException;
import com.moyeorun.auth.global.common.response.MessageResponseDto;
import com.moyeorun.auth.global.config.property.JwtProperty;
import com.moyeorun.auth.global.security.exception.InvalidJwtException;
import com.moyeorun.auth.global.security.jwt.JwtClaimsVo;
import com.moyeorun.auth.global.security.jwt.JwtProvider;
import com.moyeorun.auth.global.security.jwt.JwtResolver;
import com.moyeorun.auth.global.util.RedisUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final JwtResolver jwtResolver;
  private final JwtProperty jwtProperty;
  private final RedisUtil redisUtil;

  @Transactional
  public SignUpResponse signUp(SignUpRequest signUpRequest, SnsIdentify snsIdentify,
      String email) {
    if (userRepository.existsUserByNickName(signUpRequest.getNickName())) {
      throw new DuplicateNicknameException();
    }

    if (userRepository.existsUserBySnsIdentify(snsIdentify)) {
      throw new DuplicateSnsUserException();
    }

    User savedUser = userRepository.save(signUpRequest.toEntity(snsIdentify, email));

    TokenDto tokenDto = createJwtToken(savedUser);

    redisUtil.setStringWithExpire(savedUser.getId().toString(), tokenDto.getRefreshToken(),
        jwtProperty.getRefresh_token_expired_time());

    return new SignUpResponse(savedUser, tokenDto);
  }

  @Transactional
  public SignInResponse signIn(SnsIdentify snsIdentify) {
    Optional<User> findUser = userRepository.findBySnsIdentify(snsIdentify);

    if (findUser.isPresent()) {
      TokenDto tokenDto = createJwtToken(findUser.get());

      redisUtil.setStringWithExpire(findUser.get().getId().toString(), tokenDto.getRefreshToken(),
          jwtProperty.getRefresh_token_expired_time());

      return new SignInResponse(findUser.get(), tokenDto);
    }
    return new SignInResponse();
  }

  @Transactional
  public RefreshResponse refresh(RefreshRequest refreshRequestDto) {
    JwtClaimsVo jwtClaims = jwtResolver.getClaimByJwt(refreshRequestDto.getAccessToken());
    String savedRefreshToken = redisUtil.getValueByStringKey(jwtClaims.getUserId());

    if (savedRefreshToken == null) {
      throw new NotSignInException();
    }

    if (!savedRefreshToken.equals(refreshRequestDto.getRefreshToken())) {

      throw new InvalidJwtException();
    }

    String accessToken = jwtProvider.createAccessToken(jwtClaims.getUserId(),
        jwtClaims.getRole());

    return new RefreshResponse(accessToken);
  }

  @Transactional
  public MessageResponseDto logout(String userId) {
    String savedRefreshToken = redisUtil.getValueByStringKey(userId);

    if (savedRefreshToken == null) {
      throw new NotSignInException();
    }
    redisUtil.deleteByStringKey(userId);
    return new MessageResponseDto("로그아웃 성공");
  }

  private TokenDto createJwtToken(User user) {
    String accessToken = jwtProvider.createAccessToken(user.getId().toString(), user.getRole());
    String refreshToken = jwtProvider.createRefreshToken();
    return new TokenDto(accessToken, refreshToken);
  }

}
