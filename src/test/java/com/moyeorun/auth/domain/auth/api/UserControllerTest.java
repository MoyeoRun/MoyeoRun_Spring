package com.moyeorun.auth.domain.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorun.auth.domain.auth.application.UserService;
import com.moyeorun.auth.domain.auth.dto.request.NicknameDuplicateRequest;
import com.moyeorun.auth.domain.auth.dto.response.NicknameDuplicateResponse;
import com.moyeorun.auth.domain.auth.dto.response.UserResponse;
import com.moyeorun.auth.global.config.SecurityConfig;
import com.moyeorun.auth.setup.domain.UserMockBuilder;
import java.util.ArrayList;
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

@WebMvcTest(controllers = UserController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest {

  @MockBean
  UserService userService;

  ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  MockMvc mockMvc;

  @DisplayName("닉네임중복체크_유효하지않는 값 실패")
  @Test
  public void nickname_실패() throws Exception {
    NicknameDuplicateRequest request = new NicknameDuplicateRequest("");
    ResultActions result = nicknameDuplicateRequest(request);
    result.andExpect(status().isBadRequest());
  }

  @DisplayName("닉네임중복체크_성공")
  @Test
  public void nickname_성공() throws Exception {
    NicknameDuplicateRequest request = new NicknameDuplicateRequest("nickname123");
    NicknameDuplicateResponse response = new NicknameDuplicateResponse(false);

    given(userService.nicknameDuplicate(any())).willReturn(response);

    ResultActions result = nicknameDuplicateRequest(request);
    result.andExpect(status().isOk());
  }

  @DisplayName("유저 조회성공")
  @Test
  void getUser_성공() throws Exception{
    Authentication authentication = new UsernamePasswordAuthenticationToken("1","", new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserResponse userResponse = new UserResponse(UserMockBuilder.ofOptional().get());

    given(userService.getUser(any())).willReturn(userResponse);

    ResultActions result = mockMvc.perform(get("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print());
    result.andExpect(status().isOk());
  }


  private ResultActions nicknameDuplicateRequest(NicknameDuplicateRequest dto) throws Exception {
    return mockMvc.perform(post("/api/user/nickname/duplicate")
            .content(objectMapper.writeValueAsBytes(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print());
  }
}
