package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  @Transactional
  public String signUp(SignUpRequest signUpRequest, SnsIdentify snsIdentify, String email) {
    if (nicknameDuplicateCheck(signUpRequest.getNickName())) {
      throw new DuplicateNicknameException();
    }

    if (snsUserDuplicateCheck(snsIdentify)) {
      throw new DuplicateSnsUserException();
    }

    userRepository.save(signUpRequest.toEntity(snsIdentify, email));
    return "회원가입 성공";
  }

  private boolean nicknameDuplicateCheck(String nickName) {
    return userRepository.existsUserByNickName(nickName);
  }

  private boolean snsUserDuplicateCheck(SnsIdentify snsIdentify) {
    return userRepository.existsUserBySnsIdentify(snsIdentify);
  }

}
