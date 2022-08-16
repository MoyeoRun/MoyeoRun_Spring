package com.moyeorun.api.domain.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.api.domain.user.application.AuthService;
import com.moyeorun.api.domain.user.domain.SnsIdentify;
import com.moyeorun.api.domain.user.domain.contant.GenderType;
import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import com.moyeorun.api.domain.user.dto.request.RefreshRequest;
import com.moyeorun.api.domain.user.dto.request.SignInRequest;
import com.moyeorun.api.domain.user.dto.request.SignUpRequest;
import com.moyeorun.api.domain.user.dto.response.RefreshResponse;
import com.moyeorun.api.domain.user.dto.response.SignInResponse;
import com.moyeorun.api.domain.user.dto.response.SignUpResponse;
import com.moyeorun.api.domain.user.dto.response.TokenDto;
import com.moyeorun.api.global.common.response.MessageResponseDto;
import com.moyeorun.api.global.config.SecurityConfig;
import com.moyeorun.api.global.security.authentication.GoogleAuthenticationIdToken;
import com.moyeorun.api.setup.domain.UserMockBuilder;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = AuthController.class)
public class AuthControllerTest {

  @MockBean
  AuthService authService;

  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  private Authentication auth;

  @BeforeEach
  void setUp() {
    auth = new GoogleAuthenticationIdToken("email@email.com",
        new SnsIdentify("123", SnsProviderType.GOOGLE), new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @DisplayName("회원가입 요청 잘못된 Body값으로 실패")
  @Test
  void singUp_유효하지않는_Body값() throws Exception {
    SignUpRequest requestDto = signUpRequestDtoInvalidValue();

    ResultActions result = requestSignUp(requestDto);

    result
        .andExpect(status().isBadRequest());
  }

  @DisplayName("회원가입 성공")
  @Test
  void singUp_성공() throws Exception {
    SignUpRequest request = signUpRequestDtoMock();
    TokenDto tokenDto = new TokenDto("accessToken", "refresToken");
    SignUpResponse response = new SignUpResponse(UserMockBuilder.ofOptional().get(), tokenDto);
    given(authService.signUp(any(), any(), any())).willReturn(response);

    ResultActions result = requestSignUp(request);

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.userId").value(1L))
        .andExpect(jsonPath("$.data.token.accessToken").value(tokenDto.getAccessToken()))
        .andExpect(jsonPath("$.data.token.refreshToken").value(tokenDto.getRefreshToken()));
  }

  @DisplayName("로그인_성공_가입이 안된 유저")
  @Test
  void singIn_성공_가입안된_유저() throws Exception {
    SignInRequest request = new SignInRequest("idtoken", SnsProviderType.GOOGLE);
    given(authService.signIn(any())).willReturn(new SignInResponse());

    ResultActions result = requestSignIn(request);

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.data.isNewUser").value(true));
  }

  @DisplayName("로그인_성공_가입된 유저")
  @Test
  void singIn_성공_가입된_유저() throws Exception {
    SignInRequest request = new SignInRequest("idtoken", SnsProviderType.GOOGLE);
    TokenDto tokenDto = new TokenDto("accessToken", "refresToken");

    given(authService.signIn(any())).willReturn(new SignInResponse(UserMockBuilder.ofOptional()
        .get(), tokenDto));

    ResultActions result = requestSignIn(request);

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.data.isNewUser").value(false))
        .andExpect(jsonPath("$.data.userId").value(UserMockBuilder.ofOptional().get().getId()));
  }


  @DisplayName("refresh_실패_잘못된 요청값")
  @Test
  void refresh_실패() throws Exception {
    RefreshRequest request = new RefreshRequest("", "refreshToken");
    String accessToken = "accessToken2";
    given(authService.refresh(any())).willReturn(new RefreshResponse(accessToken));

    mockMvc.perform(post("/api/auth/refresh")
            .content(objectMapper.writeValueAsBytes(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @DisplayName("refresh_성공")
  @Test
  void refresh_성공() throws Exception {
    RefreshRequest request = new RefreshRequest("accessToken", "refreshToken");
    String accessToken = "accessToken2";
    given(authService.refresh(any())).willReturn(new RefreshResponse(accessToken));

    mockMvc.perform(post("/api/auth/refresh")
            .content(objectMapper.writeValueAsBytes(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").value(accessToken));
  }

  @DisplayName("logOut_성공")
  @Test
  void logout_성공() throws Exception {
    Authentication authentication = new UsernamePasswordAuthenticationToken("1","", new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    given(authService.logout(any())).willReturn(new MessageResponseDto("로그아웃 성공"));

    mockMvc.perform(get("/api/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.message").value("로그아웃 성공"));
  }


  private SignUpRequest signUpRequestDtoMock() {
    return new SignUpRequest("idtokenValue", SnsProviderType.GOOGLE, "imageurl..", "name1",
        "nickname1", GenderType.MALE);
  }

  private SignUpRequest signUpRequestDtoInvalidValue() {
    return new SignUpRequest("idtokenValue", SnsProviderType.GOOGLE, "", "",
        "nickname1", GenderType.MALE);
  }

  private ResultActions requestSignUp(SignUpRequest dto) throws Exception {
    return mockMvc.perform(post("/api/auth/sign-up")
            .content(objectMapper.writeValueAsBytes(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private ResultActions requestSignIn(SignInRequest dto) throws Exception {
    return mockMvc.perform(post("/api/auth/sign-in")
            .content(objectMapper.writeValueAsBytes(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

}
