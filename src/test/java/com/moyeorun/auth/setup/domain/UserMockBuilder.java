package com.moyeorun.auth.setup.domain;

import com.moyeorun.auth.domain.auth.domain.SnsIdentify;
import com.moyeorun.auth.domain.auth.domain.User;
import com.moyeorun.auth.domain.auth.domain.contant.GenderType;
import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

public class UserMockBuilder {

  public static Optional<User> ofOptional() {
    User user = User.builder()
        .email("email@email.com")
        .snsIdentify(new SnsIdentify("12345", SnsProviderType.GOOGLE))
        .gender(GenderType.MALE)
        .nickName("nickname..")
        .name("name..")
        .image("imageurl..")
        .build();
    ReflectionTestUtils.setField(user, "id", 1L);
    return Optional.of(user);
  }
}
