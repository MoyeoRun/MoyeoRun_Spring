package com.moyeorun.api.global.config;

import com.moyeorun.api.domain.user.domain.contant.RoleType;
import com.moyeorun.api.global.common.AuthUser;
import com.moyeorun.api.global.common.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isAuthUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
    boolean isAuthUserClass = AuthUser.class.equals(parameter.getParameterType());

    return isAuthUserAnnotation && isAuthUserClass;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = Long.valueOf(authentication.getPrincipal().toString());
    String role = authentication.getAuthorities().stream().toList().get(0).getAuthority();
    RoleType userRole = RoleType.valueOf(role);

    return new AuthUser(userId, userRole);
  }
}
