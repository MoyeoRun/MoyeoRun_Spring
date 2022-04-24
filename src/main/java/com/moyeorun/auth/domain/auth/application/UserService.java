package com.moyeorun.auth.domain.auth.application;

import com.moyeorun.auth.domain.auth.dao.UserRepository;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.dto.request.NicknameDuplicateRequest;
import com.moyeorun.auth.domain.auth.dto.response.NicknameDuplicateResponse;
import com.moyeorun.auth.domain.auth.dto.response.UserResponse;
import com.moyeorun.auth.domain.auth.exception.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public NicknameDuplicateResponse nicknameDuplicate(NicknameDuplicateRequest duplicateRequest) {
    return new NicknameDuplicateResponse(
        userRepository.existsUserByNickName(duplicateRequest.getNickName()));
  }

  public UserResponse getUser(Long id) {
    Optional<User> findUser = userRepository.findById(id);

    if(findUser.isPresent()){
      return new UserResponse(findUser.get());
    }
    throw new UserNotFoundException();
  }
}
