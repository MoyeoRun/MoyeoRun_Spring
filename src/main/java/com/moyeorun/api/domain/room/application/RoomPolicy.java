package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.user.domain.User;

public interface RoomPolicy {

  void constraintRoomCheck(User user);
}
