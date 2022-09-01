package com.moyeorun.api.domain.running.api;

import com.moyeorun.api.domain.running.dto.TestDto;
import com.moyeorun.api.global.error.ErrorCode;
import com.moyeorun.api.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RunningController {


  private final SimpMessageSendingOperations messagingTemplate;

  /*
   *  /pub/chat 으로 오는 경우
   * /topic/chat 으로 리턴한다.
   */
  @MessageMapping("/chat")
  public void test(@Payload TestDto dto) {
    log.info("/chat 엔드포인트, 값 : " + dto.toString());
    messagingTemplate.convertAndSend("/topic/chat", dto.toString());
  }

  /*
  *  Error Test용 EndPoint.
  */
  @MessageMapping("/error")
  public void solo(@Payload String message) {
    log.info("받은 값 : " + message);
    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
  }

  /*
   *  SentToUser : 에러를 발생시킨 Session으로 연결된 클라이언트에게 메시지를 보낸다.
   *  클라이언트는 /user/queue/errors 로 구독을 해야 한다.
   */
  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(BusinessException exception) {
    log.error("에러 발생!");
    return exception.getMessage();
  }
}
