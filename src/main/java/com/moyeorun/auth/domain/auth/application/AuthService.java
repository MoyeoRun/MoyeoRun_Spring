package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.SignUpRequest;
import com.moyeorun.auth.domain.auth.exception.DuplicateNicknameException;
import com.moyeorun.auth.domain.auth.exception.DuplicateSnsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  @Transactional
  public User signUp(SignUpRequest signUpRequest, SnsIdentify snsIdentify, String email) {
    nicknameDuplicateCheck(signUpRequest.getNickName());
    snsUserDuplicateCheck(snsIdentify);

    return userRepository.save(signUpRequest.toEntity(snsIdentify, email));
  }

  private void nicknameDuplicateCheck(String nickName) {
    if (userRepository.existsUserByNickName(nickName)) {
      throw new DuplicateNicknameException();
    }
  }

  private void snsUserDuplicateCheck(SnsIdentify snsIdentify) {
    if (userRepository.existsUserBySnsIdentify(snsIdentify)) {
      throw new DuplicateSnsUser();
    }
  }

}
