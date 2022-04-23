package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.dto.response.SignInResponse;
import com.moyeorun.auth.domain.auth.dto.response.SignUpResponse;
import com.moyeorun.auth.domain.auth.dto.response.TokenDto;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import com.moyeorun.auth.global.config.property.JwtProperty;
import com.moyeorun.auth.global.security.jwt.JwtProvider;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final StringRedisTemplate stringRedisTemplate;
  private final JwtProperty jwtProperty;

  @Transactional
  public SignUpResponse signUp(SignUpRequest signUpRequest, SnsIdentify snsIdentify, String email) {
    if (nicknameDuplicateCheck(signUpRequest.getNickName())) {
      throw new DuplicateNicknameException();
    }

    if (snsUserDuplicateCheck(snsIdentify)) {
      throw new DuplicateSnsUserException();
    }

    userRepository.save(signUpRequest.toEntity(snsIdentify, email));
    return new SignUpResponse("회원가입 성공");
  }

  @Transactional
  public SignInResponse signIn(SnsIdentify snsIdentify) {
    Optional<User> findUser = userRepository.findBySnsIdentify(snsIdentify);

    if (findUser.isPresent()) {
      String accessToken = jwtProvider.createAccessToken(findUser.get());
      String refreshToken = jwtProvider.createRefreshToken(findUser.get());

      ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
      valueOperations.set(refreshToken, findUser.get().getId().toString());
      stringRedisTemplate.expire(refreshToken, jwtProperty.getRefresh_token_expired_time(),
          TimeUnit.SECONDS);

      return new SignInResponse(findUser.get(), new TokenDto(accessToken, refreshToken));
    } else {
      return new SignInResponse();
    }
  }

  private boolean nicknameDuplicateCheck(String nickName) {
    return userRepository.existsUserByNickName(nickName);
  }

  private boolean snsUserDuplicateCheck(SnsIdentify snsIdentify) {
    return userRepository.existsUserBySnsIdentify(snsIdentify);
  }

}
