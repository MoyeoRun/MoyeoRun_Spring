package com.moyeorun.api.domain.room.api;

import com.moyeorun.api.domain.room.application.RoomGetService;
import com.moyeorun.api.domain.room.application.RoomService;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.domain.room.dto.response.RoomResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final RoomGetService roomGetService;
  @GetMapping("/api/room/{id}")
  public ResponseEntity<?> getOne(@PathVariable("id") Long roomId){
    RoomResponse result = roomGetService.getOne(roomId);
    return SuccessResponse.successWidthData(result);
  }


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

  @PostMapping("/api/room/{id}/join")
  public ResponseEntity<?> join(@LoginUser AuthUser user,
      @PathVariable("id") @NotNull @Min(1) Long roomId){
    roomService.joinRoom(user.getUserId(), roomId);
    return SuccessResponse.successWidthData(new MessageResponseDto("방 참여 성공"));
  }

  @DeleteMapping("/api/room/{id}/join")
  public ResponseEntity<?> cancelJoin(@LoginUser AuthUser user,
      @PathVariable("id") @NotNull @Min(1) Long roomId){
    roomService.joinCancel(user.getUserId(), roomId);
    return SuccessResponse.successWidthData(new MessageResponseDto("방 참여 취소 성공"));
  }
}
