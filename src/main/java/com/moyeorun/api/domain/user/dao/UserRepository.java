package com.moyeorun.api.domain.user.dao;

import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findBySnsIdentify(SnsIdentify snsIdentify);

  boolean existsUserBySnsIdentify(SnsIdentify snsIdentify);

  boolean existsUserByNickName(String nickName);
}
