package com.moyeorun.auth.global.config;

import com.moyeorun.auth.global.security.filter.IdTokenExceptionFilter;
import com.moyeorun.auth.global.security.filter.IdTokenAuthenticationFilter;
import com.moyeorun.auth.global.security.matcher.IdTokenFilterMatcher;
import com.moyeorun.auth.global.security.provider.GoogleIdTokenAuthenticationProvider;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  private final GoogleIdTokenAuthenticationProvider googleIdTokenAuthenticationProvider;

  protected IdTokenExceptionFilter idTokenExceptionFilter() throws Exception {

    return new IdTokenExceptionFilter();
  }

  protected IdTokenAuthenticationFilter idTokenSignInFilter() throws Exception {
    List<AntPathRequestMatcher> applyPath = new ArrayList<>();
    applyPath.add(new AntPathRequestMatcher("/api/auth/sign-in", HttpMethod.POST.name()));
        applyPath.add(new AntPathRequestMatcher("/api/auth/sign-up", HttpMethod.POST.name()));

    IdTokenFilterMatcher filterMatcher = new IdTokenFilterMatcher(applyPath);

    IdTokenAuthenticationFilter idTokenSignInFilter = new IdTokenAuthenticationFilter(filterMatcher);

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
    http.addFilterBefore(idTokenSignInFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(idTokenExceptionFilter(), IdTokenAuthenticationFilter.class);
    http
        .csrf().disable()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/test").permitAll()
        .antMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated();


  }
}
