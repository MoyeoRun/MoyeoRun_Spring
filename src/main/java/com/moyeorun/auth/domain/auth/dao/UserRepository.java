package com.moyeorun.auth.domain.auth.dao;

import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findBySnsIdentify(SnsIdentify snsIdentify);
}
