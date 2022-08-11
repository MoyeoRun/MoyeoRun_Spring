package com.moyeorun.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.api.global.security.filter.ReusableRequestWrapperFilter;
import com.moyeorun.api.global.security.filter.IdTokenAuthenticationFilter;
import com.moyeorun.api.global.security.filter.JwtAuthenticationFilter;
import com.moyeorun.api.global.security.filter.JwtExceptionFilter;
import com.moyeorun.api.global.security.handler.JwtAccessDeniedHandler;
import com.moyeorun.api.global.security.handler.JwtAuthenticationEntryPoint;
import com.moyeorun.api.global.security.jwt.JwtResolver;
import com.moyeorun.api.global.security.matcher.IdTokenFilterMatcher;
import com.moyeorun.api.global.security.provider.GoogleIdTokenAuthenticationProvider;
import com.moyeorun.api.global.util.HeaderTokenExtractor;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  private final GoogleIdTokenAuthenticationProvider googleIdTokenAuthenticationProvider;
  private final JwtResolver jwtResolver;
  private final JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final ObjectMapper objectMapper;
  private final HeaderTokenExtractor headerTokenExtractor;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  protected ReusableRequestWrapperFilter idTokenExceptionFilter() throws Exception {
    return new ReusableRequestWrapperFilter();
  }

  protected JwtExceptionFilter jwtExceptionFilter() throws Exception {
    return new JwtExceptionFilter(objectMapper);
  }

  protected JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {

    return new JwtAuthenticationFilter(jwtResolver, headerTokenExtractor);
  }

  protected IdTokenAuthenticationFilter idTokenAuthenticationFilter() throws Exception {
    List<AntPathRequestMatcher> applyPath = new ArrayList<>();
    applyPath.add(new AntPathRequestMatcher("/api/auth/sign-in", HttpMethod.POST.name()));
    applyPath.add(new AntPathRequestMatcher("/api/auth/sign-up", HttpMethod.POST.name()));

    IdTokenFilterMatcher filterMatcher = new IdTokenFilterMatcher(applyPath);

    IdTokenAuthenticationFilter idTokenSignInFilter = new IdTokenAuthenticationFilter(
        filterMatcher, objectMapper);

    idTokenSignInFilter.setAuthenticationManager(super.authenticationManagerBean());
    return idTokenSignInFilter;
  }

  @Bean
  public AuthenticationManager getAuthenticationManger() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth
        .authenticationProvider(this.googleIdTokenAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(idTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(idTokenExceptionFilter(), IdTokenAuthenticationFilter.class);

    http.addFilterAfter(jwtAuthenticationFilter(), IdTokenAuthenticationFilter.class);
    http.addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter.class);

    http
        .csrf().disable()
        .formLogin().disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

  }
}
