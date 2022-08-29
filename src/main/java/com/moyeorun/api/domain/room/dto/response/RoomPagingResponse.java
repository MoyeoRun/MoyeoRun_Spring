package com.moyeorun.api.domain.room.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomPagingResponse {

  private Boolean lastPage;
  private List<RoomResponse> roomList;
}
