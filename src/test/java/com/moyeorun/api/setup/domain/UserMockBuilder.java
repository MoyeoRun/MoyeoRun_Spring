package com.moyeorun.api.setup.domain;

import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.domain.user.domain.contant.GenderType;
import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
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
