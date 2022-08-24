package com.moyeorun.api.domain.room.api;

import com.moyeorun.api.domain.room.application.RoomService;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.global.common.AuthUser;
import com.moyeorun.api.global.common.LoginUser;
import com.moyeorun.api.global.common.response.MessageResponseDto;
import com.moyeorun.api.global.common.response.SuccessResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping("/api/room")
  public ResponseEntity<?> createRoom(@LoginUser AuthUser user,
      @RequestBody @Valid CreateRoomRequest request
  ) {
    CreateRoomResponse result = roomService.createRoom(request, user.getUserId());
    return SuccessResponse.successWidthData(result);
  }

  @PostMapping("/api/room/{id}/reservation")
  public ResponseEntity<?> reserve(@LoginUser AuthUser user,
      @PathVariable("id") @NotNull @Min(1) Long roomId) {
    roomService.reserve(user.getUserId(), roomId);
    return SuccessResponse.successWidthData(new MessageResponseDto("방 예약에 성공했습니다."));
  }

  @DeleteMapping("/api/room/{id}/reservation")
  public ResponseEntity<?> cancelReservation(@LoginUser AuthUser user,
      @PathVariable("id") @NotNull Long roomId) {
    roomService.cancelReservation(user.getUserId(), roomId);
    return SuccessResponse.successWidthData(new MessageResponseDto("방 예약을 취소했습니다."));
  }
}
