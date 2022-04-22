package com.moyeorun.auth.global.security.authentication;

import com.moyeorun.auth.domain.auth.domain.SnsIdentify;

public interface IdTokenAuthentication {

  SnsIdentify getSnsIdentify();

  String getEmail();
}
