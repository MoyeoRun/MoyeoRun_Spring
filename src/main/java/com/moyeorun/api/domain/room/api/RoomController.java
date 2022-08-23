package com.moyeorun.api.domain.room.api;

import com.moyeorun.api.domain.room.application.RoomService;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.global.common.AuthUser;
import com.moyeorun.api.global.common.LoginUser;
import com.moyeorun.api.global.common.response.SuccessResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping("/api/room")
  public ResponseEntity<?> createRoom(@LoginUser AuthUser user,
      @RequestBody @Valid CreateRoomRequest request
      ){
    CreateRoomResponse result = roomService.createRoom(request, user.getUserId());
    return SuccessResponse.successWidthData(result);
  }
}
