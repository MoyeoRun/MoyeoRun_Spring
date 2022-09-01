package com.moyeorun.api.domain.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomResponse {
  private String message;
  private Long roomId;
}
