package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.dto.request.NicknameDuplicateRequest;
import com.moyeorun.auth.domain.auth.dto.response.NicknameDuplicateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public NicknameDuplicateResponse nicknameDuplicate(NicknameDuplicateRequest duplicateRequest) {
    return new NicknameDuplicateResponse(userRepository.existsUserByNickName(duplicateRequest.getNickName()));
  }
}
