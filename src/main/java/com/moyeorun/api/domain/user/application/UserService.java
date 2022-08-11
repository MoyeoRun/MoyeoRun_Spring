package com.moyeorun.api.domain.user.application;

import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.domain.user.dto.request.NicknameDuplicateRequest;
import com.moyeorun.api.domain.user.dto.response.NicknameDuplicateResponse;
import com.moyeorun.api.domain.user.dto.response.UserResponse;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public NicknameDuplicateResponse nicknameDuplicate(NicknameDuplicateRequest duplicateRequest) {
    return new NicknameDuplicateResponse(
        userRepository.existsUserByNickName(duplicateRequest.getNickName()));
  }

  @Transactional(readOnly = true)
  public UserResponse getUser(Long id) {
    Optional<User> findUser = userRepository.findById(id);

    if (findUser.isPresent()) {
      return new UserResponse(findUser.get());
    }
    throw new EntityNotFoundException();
  }
}
