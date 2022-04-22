package com.moyeorun.auth.domain.auth.dao;

import com.moyeorun.auth.domain.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
