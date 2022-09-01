package com.moyeorun.api.global.config;


import com.moyeorun.api.domain.running.handler.StompHandler;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final StompHandler stompHandler;
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic/", "/queue/");
    config.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);

  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/api/ws").setAllowedOriginPatterns("*")
        .setHandshakeHandler(new DefaultHandshakeHandler() {

          public boolean beforeHandshake(
              ServerHttpRequest request,
              ServerHttpResponse response,
              WebSocketHandler wsHandler,
              Map attributes) throws Exception {
            log.info("연결 성공");
            if (request instanceof ServletServerHttpRequest) {
              ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
              HttpSession session = servletRequest
                  .getServletRequest().getSession();
              attributes.put("sessionId", session.getId());
            }
            return true;
          }
        });

  }
}
